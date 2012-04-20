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
package org.castor.jpa.scenario.many_to_many;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.exolab.castor.jdo.JDOManager;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is part of the functional test suite for Castor JDO
 * and assists in testing JPA annotation support.
 * 
 * @author Werner Guttmann
 * @since 1.3.1
 */
// @Ignore
@ContextConfiguration(locations = { "spring-config.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AuthorCastorDaoTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JDOManager jdoManager;

    @Autowired
    private AuthorDao authorDao;

    @Test
    @Transactional
    public void saveAuthorOnly() {
        Author author = new Author();
        author.setId(1234);
        author.setName("First");
        author.setBooks(new LinkedList<Book>());

        this.authorDao.saveAuthor(author);

        Author got = this.authorDao.getAuthor(1234);
        assertNotNull(got);
        assertEquals("First", got.getName());

    }
}
