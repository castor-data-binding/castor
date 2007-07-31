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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.engine.OQLQueryImpl;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;


/**
 * A simple Query tool. With this tool you can perform interactive
 * OQL Queries against a Castor Database.
 *
 * @author <a href="mauch@imkenberg.de">Thorsten Mauch</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class QueryAnalyser {
    boolean _packFrame = false;

    /**Construct the application*/
    private QueryAnalyser(final String databasename, final String dbconfig) {
        MainFrame frame = new MainFrame(databasename, dbconfig);
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (_packFrame) {
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
    public static void main(final String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: org.exolab.castor.tools.QueryAnalyser <Databasename> <Databaseconfig>");
            System.out.println("Example: org.exolab.castor.tools.QueryAnalyser testdb database.xml");
            System.exit(1);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new QueryAnalyser(args[0], args[1]);
    }

    private class MainFrame extends JFrame {
        /** SerialVersionUID */
        private static final long serialVersionUID = 3524368558606642742L;

        QueryHistory _qhistory = new QueryHistory();
        Mapping _mapping = new Mapping();

        JPanel _contentPane;
        BorderLayout _borderLayout1 = new BorderLayout();
        DefaultTableModel _model;

        JDOManager _jdo;
        String _dbName;
        String _dbConf;
        JTabbedPane _tabbedPane = new JTabbedPane();
        JToolBar _toolbar = new JToolBar();
        JButton _btnNext = new JButton();
        JButton _btnExit = new JButton();
        JPanel _sqlresult = new JPanel();
        JTextPane _sqlPane = new JTextPane();
        BorderLayout _borderLayout3 = new BorderLayout();
        JTextPane _oqlquery = new JTextPane();
        JPanel _queryPanel = new JPanel();
        BorderLayout _borderLayout2 = new BorderLayout();
        JScrollPane _resultScrollpane = new JScrollPane();
        JTable _resultTable = new JTable();
        JButton _execute = new JButton();
        JScrollPane _errorScrollPane = new JScrollPane();
        JPanel _errorPanel = new JPanel();
        JTextPane _oqlerror = new JTextPane();
        BorderLayout _borderLayout4 = new BorderLayout();
        JButton _btnPrevious = new JButton();
        JLabel _statusBar = new JLabel();

        /**Construct the frame*/
        public MainFrame(final String dbName, final String dbConf) {
            _dbName = dbName;
            _dbConf = dbConf;

            enableEvents(AWTEvent.WINDOW_EVENT_MASK);
            try {
                jbInit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        /**Component initialization*/
        private void jbInit() throws Exception    {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            //setIconImage(Toolkit.getDefaultToolkit().createImage(testframe.class.getResource("[Your Icon]")));
            _contentPane = (JPanel) this.getContentPane();
            _contentPane.setLayout(_borderLayout1);
            this.setSize(new Dimension(600, 400));
            this.setTitle("Castor OQL-Ouery Analyser");
            //ResultScrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            //ResultScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            _sqlresult.setLayout(_borderLayout3);
            _oqlquery.setFont(new java.awt.Font("Dialog", 0, 12));
            _oqlquery.setToolTipText("create Query here");
            _queryPanel.setLayout(_borderLayout2);
            _resultScrollpane.setToolTipText("");
            _resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            _btnExit.setMaximumSize(new Dimension(50, 39));
            _btnExit.setMinimumSize(new Dimension(50, 39));
            _btnExit.setActionCommand("");
            _btnExit.setIcon(new ImageIcon(cl.getResource("org/exolab/castor/gui/images/exit.gif")));
            _btnExit.setMnemonic('0');

            _btnExit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                        exit();
                }
            });

            _execute.setMaximumSize(new Dimension(50, 39));
            _execute.setMinimumSize(new Dimension(50, 39));
            _execute.setActionCommand("");
            _execute.setIcon(new ImageIcon(cl.getResource("org/exolab/castor/gui/images/fire.gif")));
            _execute.setMnemonic('0');
            _execute.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    executeActionPerformed(e);
                }
            });
            _errorPanel.setLayout(_borderLayout4);
            _btnPrevious.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    btnPreviousActionPerformed(e);
                }
            });
            _btnNext.setMaximumSize(new Dimension(50, 39));
            _btnNext.setMinimumSize(new Dimension(50, 39));
            _btnNext.setActionCommand("");
            _btnNext.setIcon(new ImageIcon(cl.getResource("org/exolab/castor/gui/images/arrw04e.gif")));
            _btnNext.setMnemonic('0');
            _btnNext.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    btnNextActionPerformed(e);
                }
            });
            _btnPrevious.setMaximumSize(new Dimension(50, 39));
            _btnPrevious.setMinimumSize(new Dimension(50, 39));
            _btnPrevious.setToolTipText("Goto previous Query");
            _btnPrevious.setIcon(new ImageIcon(cl.getResource("org/exolab/castor/gui/images/arrw04d.gif")));
            _btnPrevious.setMnemonic('0');
            _contentPane.add(_tabbedPane, BorderLayout.CENTER);
            _contentPane.add(_toolbar, BorderLayout.NORTH);

            _toolbar.add(_btnExit, null);
            _toolbar.add(_execute, null);
            _toolbar.add(_btnPrevious, null);
            _toolbar.add(_btnNext, null);
            _contentPane.add(_statusBar, BorderLayout.SOUTH);
            _tabbedPane.add(_queryPanel, "OQL Query");
            _queryPanel.add(_oqlquery, BorderLayout.CENTER);
            _tabbedPane.add(_resultScrollpane, "Resultset");
            _tabbedPane.add(_errorScrollPane, "Stacktrace");
            _errorScrollPane.getViewport().add(_errorPanel, null);
            _errorPanel.add(_oqlerror, BorderLayout.CENTER);
            _tabbedPane.add(_sqlresult, "SQL");
            _sqlresult.add(_sqlPane, BorderLayout.CENTER);



            _resultScrollpane.getViewport().add(_resultTable, null);
            // Open the Database
            openDB();
            _statusBar.setText("Database " + _jdo.getDatabaseName() + " waiting for Queries");
            // Load the query history
            loadHistory();
            _oqlquery.setText(_qhistory.getCurrentQuery());
        }
        /**Overridden so we can exit when window is closed*/
        protected void processWindowEvent(final WindowEvent e) {
            super.processWindowEvent(e);
            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                exit();

            }
        }

        void executeActionPerformed(final ActionEvent e) {
            performQuery();
        }

        public void performQuery() {

            OQLQuery oql;
            QueryResults r;

            boolean firstObject = true;
            Object o;
            Vector properties = null;
            _model = new DefaultTableModel();

            try {
                _statusBar.setText("performing Query");
                // clear results
                clearTabs();

                //ResultScrollpane.getViewport().remove(ResultScrollpane.getViewport().getComponent(0));
                // create a new conec
                Database db = _jdo.getDatabase();
                db.begin();

                 /**
                    * add query to the history,
                    * maybe it's important lo loglso querys that won't work
                    * for this reason it's logged before creation
                    */
                _qhistory.addQuery(_oqlquery.getText());


                oql = db.getOQLQuery(_oqlquery.getText());


                // and execute it
                Date starttime = new Date();
                r = oql.execute(Database.READONLY);
                Date endtime = new Date();
                // write the status bar
                _statusBar.setText("Query successful, Time: " + (endtime.getTime() - starttime.getTime()) + " ms");

                // get SQL statement via backdoor
                _sqlPane.setText(((OQLQueryImpl) oql).getSQL());


                while (r.hasMore()) {
                    o = r.next();
                    if (firstObject) {
                        properties = getProperties(o);
                        fillTableHeader(properties, _model);
                        firstObject = false;
                    }
                    _model.addRow(fillRow(properties, o));

                }
                db.commit();

                _resultTable.setModel(_model);
                _resultTable.repaint();
                _tabbedPane.setSelectedComponent(_resultScrollpane);

            } catch (Exception e) {
                // Print error into pane and status bar
                java.io.StringWriter sw = new    java.io.StringWriter();
                e.printStackTrace(new java.io.PrintWriter(sw));
                _oqlerror.setText(sw.getBuffer().toString());
                // focus to errortab
                _statusBar.setText(e.getMessage());
                //TabbedPane.setSelectedComponent(ErrorScrollPane);

            }

        }

        private Vector getProperties(final Object o) {
            Vector properties = new Vector();
            Method[] ms = o.getClass().getMethods();
            Method m;

            for (int i = 0; i < ms.length; i++) {
                m = ms[i];
                // if it begins with m and have no argument it is
                // a property
                if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                        properties.add(m);
                }
            }
            return properties;
        }

        private void fillTableHeader(final Vector properties, final DefaultTableModel model) {
            Iterator i = properties.iterator();
            Method m;
            while (i.hasNext()) {
                m = (Method) i.next();
                model.addColumn(m.getName().substring(3));

            }
        }

        private Vector fillRow(final Vector properties, final Object o) {
            Method m;
            Object temp;
            Vector results = new Vector();
            Iterator i = properties.iterator();
            while (i.hasNext()) {
                    temp = null;
                    m = (Method) i.next();
                    try {
                        temp = m.invoke(o, (Object[]) null);
                    } catch (Exception ie) {
                        temp = null;
                    }
                    results.add(temp);
            }
            return results;
        }

        private void openDB() {
            try {
                JDOManager.loadConfiguration (_dbConf, ClassLoader.getSystemClassLoader());
                _jdo = JDOManager.createInstance(_dbName);
                //only to try a connection
                _jdo.getDatabase();
            } catch (MappingException pe) {
                pe.printStackTrace();
                System.exit(1);
            } catch (org.exolab.castor.jdo.PersistenceException pe) {
                pe.printStackTrace();
                System.exit(1);
            }
        }
        /**
         * Delete all content in the tabbed pane
         */
        private void clearTabs() {
            _oqlerror.setText("");
            _sqlPane.setText("");
            _resultTable.setModel(new DefaultTableModel());
        }

        void btnPreviousActionPerformed(final ActionEvent e) {
            clearTabs();
            _oqlquery.setText(_qhistory.getPreviousQuery());
            _tabbedPane.setSelectedComponent(_queryPanel);
        }

        void btnNextActionPerformed(final ActionEvent e) {
            clearTabs();
            _oqlquery.setText(_qhistory.getNextQuery());
            _tabbedPane.setSelectedComponent(_queryPanel);
        }

        public void saveHistory() {
            // write back the history to file
            try {
                FileWriter writer = new FileWriter("queryhistory.xml");
                Marshaller marshaller = new Marshaller(writer);
                marshaller.setMapping(_mapping);
                marshaller.marshal(_qhistory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void loadHistory() {
            try {
                Unmarshaller unmarshaller = new Unmarshaller(Class.forName("org.exolab.castor.gui.QueryHistory"));
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                _mapping.loadMapping(cl.getResource("org/exolab/castor/gui/Queryanlyser.xml"));
                unmarshaller.setMapping(_mapping);

                FileReader reader = new FileReader("queryhistory.xml");
                _qhistory = (QueryHistory) unmarshaller.unmarshal(reader);
            } catch (Exception e) {
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