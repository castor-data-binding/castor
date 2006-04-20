/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.gui;

import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import org.exolab.castor.jdo.JDO2;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import java.lang.Class;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.Iterator;
import java.util.Date;
import org.exolab.castor.jdo.engine.OQLQueryImpl;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;

import java.io.FileWriter;
import java.io.FileReader;


/**
 * A simple Query tool. With this tool you can perform interactive
 * OQL Queries against a Castor Database.
 *
 * @author <a href="mauch@imkenberg.de">Thorsten Mauch</a>
 * @version $Revision$ $Date$
 */
public class QueryAnalyser {
    boolean packFrame = false;

    /**Construct the application*/
    public QueryAnalyser(String databasename, String dbconfig) {
        MainFrame frame = new MainFrame(databasename,dbconfig);
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    /**Main method*/
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: org.exolab.castor.tools.QueryAnalyser <Databasename> <Databaseconfig>");
            System.out.println("Example: org.exolab.castor.tools.QueryAnalyser testdb database.xml");
            System.exit(1);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        new QueryAnalyser(args[0],args[1]);
    }

    private class MainFrame extends JFrame {
        QueryHistory qhistory = new QueryHistory();
        Mapping mapping = new Mapping();

        JPanel contentPane;
        BorderLayout borderLayout1 = new BorderLayout();
        DefaultTableModel model;

        JDO2 jdo;
        String databasename;
        String dbconfig;
        JTabbedPane TabbedPane = new JTabbedPane();
        JToolBar toolbar = new JToolBar();
        JButton btnNext = new JButton();
        JButton btnExit = new JButton();
        JPanel sqlresult = new JPanel();
        JTextPane SQLPane = new JTextPane();
        BorderLayout borderLayout3 = new BorderLayout();
        JTextPane oqlquery = new JTextPane();
        JPanel QueryPanel = new JPanel();
        BorderLayout borderLayout2 = new BorderLayout();
        JScrollPane ResultScrollpane = new JScrollPane();
        JTable ResultTable = new JTable();
        JButton execute = new JButton();
        JScrollPane ErrorScrollPane = new JScrollPane();
        JPanel ErrorPanel = new JPanel();
        JTextPane oqlerror = new JTextPane();
        BorderLayout borderLayout4 = new BorderLayout();
        JButton btnPrevious = new JButton();
        JLabel statusBar = new JLabel();

        /**Construct the frame*/
        public MainFrame(String _databasename,String _dbconfig) {
            databasename=_databasename;
            dbconfig=_dbconfig;

            enableEvents(AWTEvent.WINDOW_EVENT_MASK);
            try {
                jbInit();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        /**Component initialization*/
        private void jbInit() throws Exception    {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            //setIconImage(Toolkit.getDefaultToolkit().createImage(testframe.class.getResource("[Your Icon]")));
            contentPane = (JPanel) this.getContentPane();
            contentPane.setLayout(borderLayout1);
            this.setSize(new Dimension(600, 400));
            this.setTitle("Castor OQL-Ouery Analyser");
            //ResultScrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            //ResultScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            sqlresult.setLayout(borderLayout3);
            oqlquery.setFont(new java.awt.Font("Dialog", 0, 12));
            oqlquery.setToolTipText("create Query here");
            QueryPanel.setLayout(borderLayout2);
            ResultScrollpane.setToolTipText("");
            ResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            btnExit.setMaximumSize(new Dimension(50, 39));
            btnExit.setMinimumSize(new Dimension(50, 39));
            btnExit.setActionCommand("");
            btnExit.setIcon(new ImageIcon(cl.getResource("org/exolab/castor/gui/images/exit.gif")));
            btnExit.setMnemonic('0');

            btnExit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                        exit();
                }
            });

            execute.setMaximumSize(new Dimension(50, 39));
            execute.setMinimumSize(new Dimension(50, 39));
            execute.setActionCommand("");
            execute.setIcon(new ImageIcon(cl.getResource("org/exolab/castor/gui/images/fire.gif")));
            execute.setMnemonic('0');
            execute.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    execute_actionPerformed(e);
                }
            });
            ErrorPanel.setLayout(borderLayout4);
            btnPrevious.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnPrevious_actionPerformed(e);
                }
            });
            btnNext.setMaximumSize(new Dimension(50, 39));
            btnNext.setMinimumSize(new Dimension(50, 39));
            btnNext.setActionCommand("");
            btnNext.setIcon(new ImageIcon(cl.getResource("org/exolab/castor/gui/images/arrw04e.gif")));
            btnNext.setMnemonic('0');
            btnNext.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnNext_actionPerformed(e);
                }
            });
            btnPrevious.setMaximumSize(new Dimension(50, 39));
            btnPrevious.setMinimumSize(new Dimension(50, 39));
            btnPrevious.setToolTipText("Goto previous Query");
            btnPrevious.setIcon(new ImageIcon(cl.getResource("org/exolab/castor/gui/images/arrw04d.gif")));
            btnPrevious.setMnemonic('0');
            contentPane.add(TabbedPane, BorderLayout.CENTER);
            contentPane.add(toolbar, BorderLayout.NORTH);

            toolbar.add(btnExit, null);
            toolbar.add(execute, null);
            toolbar.add(btnPrevious, null);
            toolbar.add(btnNext, null);
            contentPane.add(statusBar, BorderLayout.SOUTH);
            TabbedPane.add(QueryPanel, "OQL Query");
            QueryPanel.add(oqlquery, BorderLayout.CENTER);
            TabbedPane.add(ResultScrollpane, "Resultset");
            TabbedPane.add(ErrorScrollPane, "Stacktrace");
            ErrorScrollPane.getViewport().add(ErrorPanel, null);
            ErrorPanel.add(oqlerror, BorderLayout.CENTER);
            TabbedPane.add(sqlresult, "SQL");
            sqlresult.add(SQLPane, BorderLayout.CENTER);



            ResultScrollpane.getViewport().add(ResultTable, null);
            // Open the Database
            openDB();
            statusBar.setText("Database " +jdo.getDatabaseName() +" waiting for Queries");
            // Load the query history
            loadHistory();
            oqlquery.setText(qhistory.GetCurrentQuery());
        }
        /**Overridden so we can exit when window is closed*/
        protected void processWindowEvent(WindowEvent e) {
            super.processWindowEvent(e);
            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                exit();

            }
        }

        void execute_actionPerformed(ActionEvent e) {
            performQuery();
        }

        public void performQuery() {

            OQLQuery oql;
            QueryResults r ;

            boolean firstObject = true;
            Object o;
            Vector properties=null;
            model = new DefaultTableModel();

            try{
                statusBar.setText("performing Query");
                // clear results
                clearTabs();

                //ResultScrollpane.getViewport().remove(ResultScrollpane.getViewport().getComponent(0));
                // create a new conec
                Database db = jdo.getDatabase();
                db.begin();

                 /**
                    * add query to the history,
                    * maybe it's important lo loglso querys that won't work
                    * for this reason it's logged before creation
                    */
                qhistory.addQuery(oqlquery.getText());


                oql=db.getOQLQuery(oqlquery.getText());


                // and execute it
                Date starttime = new Date();
                r=oql.execute(db.ReadOnly);
                Date endtime = new Date();
                // write the status bar
                statusBar.setText("Query successful, Time: "+ (endtime.getTime()-starttime.getTime()) +" ms");

                // get SQL statement via backdoor
                SQLPane.setText( ((OQLQueryImpl)oql).getSQL());


                while(r.hasMore()) {
                    o=r.next();
                    if (firstObject) {
                        properties=getProperties(o);
                        FillTableHeader(properties,model);
                        firstObject=false;
                    }
                    model.addRow(fillRow(properties,o));

                }
                db.commit();

                ResultTable.setModel(model);
                ResultTable.repaint();
                TabbedPane.setSelectedComponent(ResultScrollpane);

            } catch(Exception e) {
                // Print error into pane and status bar
                java.io.StringWriter sw = new    java.io.StringWriter();
                e.printStackTrace(new java.io.PrintWriter(sw));
                oqlerror.setText(sw.getBuffer().toString());
                // focus to errortab
                statusBar.setText(e.getMessage());
                //TabbedPane.setSelectedComponent(ErrorScrollPane);

            }

        }

        private Vector getProperties(Object o) {
            int i;
            Vector properties=new Vector();
            Method ms[] = o.getClass().getMethods();
            Method m;

            for(i=0;i<ms.length;i++) {
                m=ms[i];
                // if it begins with m and have no argument it is
                // a property
                if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                        properties.add(m);
                }
            }
            return properties;
        }

        private void FillTableHeader(Vector properties, DefaultTableModel model) {
            Iterator i = properties.iterator();
            int col=0;
            TableColumn tc;
            Method m;
            while(i.hasNext()) {
                m=(Method)i.next();
                model.addColumn(m.getName().substring(3));

            }
        }

        private Vector fillRow(Vector properties, Object o) {
            Method m;
            Object temp;
            Vector results=new Vector();
            Iterator i =properties.iterator();
            while(i.hasNext()) {
                    temp=null;
                    m=(Method)i.next();
                    try{
                        temp=m.invoke(o,null);
                    } catch(Exception ie) {
                        temp =null;
                    }
                    results.add(temp);
            }
            return results;
        }

        private void openDB() {
            try{
                JDO2.loadConfiguration (dbconfig, ClassLoader.getSystemClassLoader());
                jdo = JDO2.createInstance(databasename);
                //only to try a connection
                Database db = jdo.getDatabase();
            } 
            catch (MappingException pe) {
                pe.printStackTrace();
                System.exit(1);
            } 
            catch(org.exolab.castor.jdo.PersistenceException pe) {
                pe.printStackTrace();
                System.exit(1);
            }
        }
        /**
         * Delete all content in the tabbed pane
         */
        private void clearTabs() {
            oqlerror.setText("");
            SQLPane.setText("");
            ResultTable.setModel(new DefaultTableModel());
        }

        void btnPrevious_actionPerformed(ActionEvent e) {
            clearTabs();
            oqlquery.setText(qhistory.getPreviousQuery());
            TabbedPane.setSelectedComponent(QueryPanel);
        }

        void btnNext_actionPerformed(ActionEvent e) {
            clearTabs();
            oqlquery.setText(qhistory.getNextQuery());
            TabbedPane.setSelectedComponent(QueryPanel);
        }

        public void saveHistory() {
            // write back the history to file
            try{
                FileWriter writer = new FileWriter("queryhistory.xml");
                Marshaller marshaller = new Marshaller(writer);
                marshaller.setMapping(mapping);
                marshaller.marshal(qhistory);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        private void loadHistory() {
            try{
                Unmarshaller unmarshaller = new Unmarshaller(Class.forName("org.exolab.castor.gui.QueryHistory"));
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                mapping.loadMapping(cl.getResource("org/exolab/castor/gui/Queryanlyser.xml"));
                unmarshaller.setMapping(mapping);

                FileReader reader = new FileReader("queryhistory.xml");
                qhistory=(QueryHistory) unmarshaller.unmarshal(reader);
            } catch(Exception e) {
                e.printStackTrace();
                // if there is no file, it's ok also
                // then we have an empty History
            }
        }

        void exit() {
            saveHistory();
            System.exit(0);
        }
    }
}