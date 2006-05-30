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
package ptf.jdo.rel1toN;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-06-24 19:41:08 -0600 (Fri, 24 Jun 2005) $
 */
public final class State {
    //-------------------------------------------------------------------------

    private Integer         _id;
    private String          _name;
    private Locked          _locked;
    private boolean         _input = false;
    private boolean         _output = false;
    private boolean         _service = false;
    private boolean         _changeFrom = true;
    private boolean         _changeTo = true;
    private Collection      _departments = new ArrayList();
    private Collection      _equipments = new ArrayList();
    private String          _note;
    private Date            _createdAt;
    private String          _createdBy;
    private Date            _updatedAt;
    private String          _updatedBy;

    //-------------------------------------------------------------------------
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }
    
    public Locked getLocked() { return _locked; }
    public void setLocked(final Locked locked) { _locked = locked; }
    
    public boolean getInput() { return _input; }
    public void setInput(final boolean input) { _input = input; }
    
    public boolean getOutput() { return _output; }
    public void setOutput(final boolean output) { _output = output; }
    
    public boolean getService() { return _service; }
    public void setService(final boolean service) { _service = service; }
    
    public boolean getChangeFrom() { return _changeFrom; }
    public void setChangeFrom(final boolean changeFrom) { _changeFrom = changeFrom; }
    
    public boolean getChangeTo() { return _changeTo; }
    public void setChangeTo(final boolean changeTo) { _changeTo = changeTo; }
    
    public Collection getDepartments() { return _departments; }
    public void setDepartments(final Collection departments) {
        _departments = departments;
    }
    public void addDepartment(final Department department) {
        if ((department != null) && (!_departments.contains(department))) {
            _departments.add(department);
            department.setState(this);
        }
    }
    public void removeDepartment(final Department department) {
        if ((department != null) && (_departments.contains(department))) {
            _departments.remove(department);
            department.setState(null);
        }
    }
    
    public Collection getEquipments() { return _equipments; }
    public void setEquipments(final Collection equipments) {
        _equipments = equipments;
    }
    public void addEquipment(final Equipment equipment) {
        if ((equipment != null) && (!_equipments.contains(equipment))) {
            _equipments.add(equipment);
            equipment.setState(this);
        }
    }
    public void removeEquipment(final Equipment equipment) {
        if ((equipment != null) && (_equipments.contains(equipment))) {
            _equipments.remove(equipment);
            equipment.setState(null);
        }
    }

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
        
        sb.append("<State id='"); sb.append(_id);
        sb.append("' name='"); sb.append(_name);
        sb.append("' input='"); sb.append(_input);
        sb.append("' output='"); sb.append(_output);
        sb.append("' service='"); sb.append(_service);
        sb.append("' changeFrom='"); sb.append(_changeFrom);
        sb.append("' changeTo='"); sb.append(_changeTo);
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

        sb.append(_locked);
        
        sb.append("</State>\n");

        return sb.toString();
    }
    
    //-------------------------------------------------------------------------
}
