/*
 * Copyright 2009 Werner Guttmann
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
package org.castor.jpa.scenario.one_to_many;

import org.castor.spring.orm.support.CastorDaoSupport;

/**
 * This class is part of the functional test suite for Castor JDO
 * and assists in testing JPA annotation support.
 * 
 * @author Werner Guttmann
 * @since 1.3.1
 */
public class ActorCastorDao extends CastorDaoSupport implements ActorDao {

    public void deleteActor(Actor actor) {
        this.getCastorTemplate().remove(actor);
    }

    public Actor getActor(int id) {
        return (Actor) this.getCastorTemplate().load(Actor.class,
                new Integer(id));
    }

    public void saveActor(Actor actor) {
        this.getCastorTemplate().create(actor);

    }

}
