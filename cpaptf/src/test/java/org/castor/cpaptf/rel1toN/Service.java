/*
 * Copyright 2005 Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpaptf.rel1toN;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision:6817 $ $Date: 2005-06-24 19:41:08 -0600 (Fri, 24 Jun 2005) $
 */
public final class Service {
    //-------------------------------------------------------------------------
    
    private Integer         _id;
    private Equipment       _equipment;
    private Integer         _number;
    private String          _name;
    private String          _description;
    private Date            _date;
    private boolean         _flag1;
    private boolean         _flag2;
    private boolean         _flag3;
    private boolean         _flag4;
    private String          _note;
    private Date            _createdAt;
    private String          _createdBy;
    private Date            _updatedAt;
    private String          _updatedBy;
    
    //-------------------------------------------------------------------------

    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }

    public Equipment getEquipment() { return _equipment; }
    public void setEquipment(final Equipment equipment) { _equipment = equipment; }
    
    public Integer getNumber() { return _number; }
    public void setNumber(final Integer number) { _number = number; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name =  name; }
    
    public String getDescription() { return _description; }
    public void setDescription(final String description) { _description = description; }
    
    public Date getDate() { return _date; }
    public void setDate(final Date date) { _date = date; }
    
    public boolean getFlag1() { return _flag1; }
    public void setFlag1(final boolean flag1) { _flag1 = flag1; }
    
    public boolean getFlag2() { return _flag2; }
    public void setFlag2(final boolean flag2) { _flag2 = flag2; }
    
    public boolean getFlag3() { return _flag3; }
    public void setFlag3(final boolean flag3) { _flag3 = flag3; }
    
    public boolean getFlag4() { return _flag4; }
    public void setFlag4(final boolean flag4) { _flag4 = flag4; }
    
    public String getNote() { return _note; }
    public void setNote(final String note) { _note = note; }
    
    public Date getCreatedAt() { return _createdAt; }
    public void setCreatedAt(final Date createdAt) { _createdAt = createdAt; }
    
    public String getCreatedBy() { return _createdBy; }
    public void setCreatedBy(final String createdBy) { _createdBy = createdBy; }
    
    public void setCreated(final Date createdAt, final String createdBy) {
        _createdAt = createdAt;
        _createdBy = createdBy;
    }

    public Date getUpdatedAt() { return _updatedAt; }
    public void setUpdatedAt(final Date updatedAt) { _updatedAt = updatedAt; }
    
    public String getUpdatedBy() { return _updatedBy; }
    public void setUpdatedBy(final String updatedBy) { _updatedBy = updatedBy; }
    
    public void setUpdated(final Date updatedAt, final String updatedBy) {
        _updatedAt = updatedAt;
        _updatedBy = updatedBy;
    }
    
    //-------------------------------------------------------------------------
    
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        StringBuffer sb = new StringBuffer();
        
        sb.append("<Service id='"); sb.append(_id);
        sb.append("' number='"); sb.append(_number);
        sb.append("' name='"); sb.append(_name);
        sb.append("' description='"); sb.append(_description);
        sb.append("' date='"); 
        if (_date != null) { 
            sb.append(df.format(_date));
        } else {
            sb.append(_date);
        }
        sb.append("' flag1='"); sb.append(_flag1);
        sb.append("' flag2='"); sb.append(_flag2);
        sb.append("' flag3='"); sb.append(_flag3);
        sb.append("' flag4='"); sb.append(_flag4);
        sb.append("' note='"); sb.append(_note);
        sb.append("' createdAt='"); 
        if (_createdAt != null) {
            sb.append(df.format(_createdAt));
        } else {
            sb.append(_createdAt);
        }
        sb.append("' createdBy='"); sb.append(_createdBy);
        sb.append("' updatedAt='"); 
        if (_updatedAt != null) { 
            sb.append(df.format(_updatedAt));
        } else {
            sb.append(_updatedAt);
        }
        sb.append("' updatedBy='"); sb.append(_updatedBy);
        sb.append("'>\n");

        sb.append(_equipment);

        sb.append("</Service>\n");
        
        return sb.toString();
    }
    
    //-------------------------------------------------------------------------
}
