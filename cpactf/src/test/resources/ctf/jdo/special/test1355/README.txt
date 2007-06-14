Testsuite: LazyLoadRelationshipTest
Tests run: 1, Failures: 0, Errors: 1, Time elapsed: 4.397 sec
------------- Standard Error -----------------
log4j:WARN No appenders could be found for logger (org.castor.jdo.engine.DatabaseRegistry).
log4j:WARN Please initialize the log4j system properly.
------------- ---------------- ---------------

Testcase: testLoadLazyCollection took 4.377 sec
	Caused an ERROR
Nested error: org.exolab.castor.jdo.PersistenceException: Object, app.GolfCourse@61a907, links to another object, app.City@1815338 that is not loaded/updated/created in this transaction: Object, app.GolfCourse@61a907, links to another object, app.City@1815338 that is not loaded/updated/created in this transaction
org.exolab.castor.jdo.TransactionAbortedException: Nested error: org.exolab.castor.jdo.PersistenceException: Object, app.GolfCourse@61a907, links to another object, app.City@1815338 that is not loaded/updated/created in this transaction: Object, app.GolfCourse@61a907, links to another object, app.City@1815338 that is not loaded/updated/created in this transaction
	at org.castor.persist.AbstractTransactionContext.prepare(AbstractTransactionContext.java:1224)
	at org.exolab.castor.jdo.engine.DatabaseImpl.commit(DatabaseImpl.java:540)
	at LazyLoadRelationshipTest.testLoadLazyCollection(LazyLoadRelationshipTest.java:118)
Caused by: org.exolab.castor.jdo.PersistenceException: Object, app.GolfCourse@61a907, links to another object, app.City@1815338 that is not loaded/updated/created in this transaction
	at org.castor.persist.resolver.PersistanceCapableRelationResolver.preStore(PersistanceCapableRelationResolver.java:238)
	at org.exolab.castor.persist.ClassMolder.preStore(ClassMolder.java:753)
	at org.exolab.castor.persist.LockEngine.preStore(LockEngine.java:755)
	at org.castor.persist.AbstractTransactionContext.prepare(AbstractTransactionContext.java:1193)
	... 17 more
Caused by: org.exolab.castor.jdo.PersistenceException: Object, app.GolfCourse@61a907, links to another object, app.City@1815338 that is not loaded/updated/created in this transaction
	at org.castor.persist.resolver.PersistanceCapableRelationResolver.preStore(PersistanceCapableRelationResolver.java:238)
	at org.exolab.castor.persist.ClassMolder.preStore(ClassMolder.java:753)
	at org.exolab.castor.persist.LockEngine.preStore(LockEngine.java:755)
	at org.castor.persist.AbstractTransactionContext.prepare(AbstractTransactionContext.java:1193)
	at org.exolab.castor.jdo.engine.DatabaseImpl.commit(DatabaseImpl.java:540)
	at LazyLoadRelationshipTest.testLoadLazyCollection(LazyLoadRelationshipTest.java:118)

