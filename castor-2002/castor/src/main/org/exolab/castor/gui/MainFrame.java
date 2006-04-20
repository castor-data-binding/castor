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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.tools;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import java.util.Properties;
import java.lang.Class;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.Iterator;

/**
 * A simple Query tool. With this tool you can perform interactive
 * OQL Queries against a Castor Database.
 *
 * @author <a href="mauch@imkenberg.de">Thorsten Mauch</a>
 * @version $Revision$ $Date$
 */
public class MainFrame extends JFrame {
  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout();
  DefaultTableModel model;
  JSplitPane jSplitPane1 = new JSplitPane();
  JScrollPane ResultScrollpane = new JScrollPane();
  JTextPane oqlquery = new JTextPane();
  JTextPane oqlerror = new JTextPane();
  JPanel QueryPanel = new JPanel();
  JButton execute = new JButton();
  BorderLayout borderLayout2 = new BorderLayout();
  JTable ResultTable = new JTable();

  JDO jdo;
  String databasename;
  String dbconfig;

  /**Construct the frame*/
  public MainFrame(String _databasename,String _dbconfig) {
    databasename=_databasename;
    dbconfig=_dbconfig;

    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**Component initialization*/
  private void jbInit() throws Exception  {


    //setIconImage(Toolkit.getDefaultToolkit().createImage(testframe.class.getResource("[Your Icon]")));
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(600, 400));
    this.setTitle("Castor OQL-Ouery Analyser");
    oqlquery.setToolTipText("create Query here");
    oqlquery.setFont(new java.awt.Font("Dialog", 0, 12));
    QueryPanel.setLayout(borderLayout2);
    execute.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        execute_actionPerformed(e);
      }
    });
    execute.setText("Query");
    execute.setActionCommand("");
    jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.setBottomComponent(ResultScrollpane);
    jSplitPane1.setLastDividerLocation(200);
    //ResultScrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    //ResultScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    ResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    ResultScrollpane.setToolTipText("");
    contentPane.add(jSplitPane1, BorderLayout.CENTER);
    jSplitPane1.add(ResultScrollpane, JSplitPane.BOTTOM);
    ResultScrollpane.getViewport().add(ResultTable,null);

    jSplitPane1.add(QueryPanel, JSplitPane.TOP);
    QueryPanel.add(execute, BorderLayout.SOUTH);
    QueryPanel.add(oqlquery, BorderLayout.CENTER);
    jSplitPane1.setDividerLocation(100);
    // Open the Database
    openDB();

  }
  /**Overridden so we can exit when window is closed*/
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void execute_actionPerformed(ActionEvent e) {
    performQuery();
  }



  public void performQuery(){


   OQLQuery oql;
   QueryResults r ;

   boolean firstObject = true;
   Object o;
   Vector properties=null;
   model = new DefaultTableModel();



     try{
      ResultScrollpane.getViewport().remove(ResultScrollpane.getViewport().getComponent(0));
      // create a new conec
      Database db = jdo.getDatabase();

      db.begin();

      oql=db.getOQLQuery(oqlquery.getText());
      r=oql.execute(db.ReadOnly);

      while(r.hasMore()){
        o=r.next();
        if(firstObject){
          properties=getProperties(o);
          FillTableHeader(properties,model);
          firstObject=false;
        }
        model.addRow(fillRow(properties,o));

      }
      db.commit();

      ResultTable.setModel(model);


      // show the Queryresults
      ResultScrollpane.getViewport().add(ResultTable,null);




    }
    catch(Exception e){
      oqlerror.setText(e.getMessage());
      e.printStackTrace();
      ResultScrollpane.getViewport().add(oqlerror,null);
    }

  }
  private Vector getProperties(Object o){
    int i;
    Vector properties=new Vector();
    Method ms[] = o.getClass().getMethods();
    Method m;

    for(i=0;i<ms.length;i++){
      m=ms[i];
      // if it begins with m and have no argument it is
      // a property
      if(m.getName().startsWith("get") && m.getParameterTypes().length == 0){
          properties.add(m);
      }
    }
    return properties;
  }

  private void FillTableHeader(Vector properties, DefaultTableModel model){
  Iterator i = properties.iterator();
  int col=0;
  TableColumn tc;
  Method m;
  while(i.hasNext()){
    m=(Method)i.next();
    model.addColumn(m.getName().substring(3));

  }

  }

  private Vector fillRow(Vector properties, Object o){
  Method m;
  Object temp;
  Vector results=new Vector();
  Iterator i =properties.iterator();
  while(i.hasNext()){
      temp=null;
      m=(Method)i.next();
      try{
        temp=m.invoke(o,null);
      }catch(Exception ie){
        temp =null;
      }
      results.add(temp);
  }
  return results;

  }

  private void openDB(){
    try{
      jdo= new JDO();
      jdo.setDatabaseName(databasename);
      jdo.setConfiguration(dbconfig);
      jdo.setClassLoader(ClassLoader.getSystemClassLoader());
      //only to try a connection
      Database db = jdo.getDatabase();
    }
    catch(org.exolab.castor.jdo.PersistenceException pe){
      pe.printStackTrace();
      System.exit(1);
    }
  }
}