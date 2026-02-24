# Maven JVM Configuration

## Purpose

This directory contains Maven JVM configuration that applies to the entire build process, including all Maven plugins.

## File: `jvm.config`

Contains JVM arguments that Maven will automatically apply when starting its own JVM process.

### Current Configuration

```
--add-opens java.xml/com.sun.org.apache.xml.internal.serialize=ALL-UNNAMED 
--add-opens java.xml/com.sun.org.apache.xpath.internal=ALL-UNNAMED 
--add-opens java.base/java.lang=ALL-UNNAMED
```

## Why This Is Needed

### Problem
Castor's integration tests (MasterTestSuite via `castor-maven-plugins:xmlctf-text`) require access to internal Java XML serialization APIs that are strongly encapsulated in Java 9+.

### Solution
These `--add-opens` arguments grant reflective access to internal JDK modules, allowing Castor's XML marshalling framework to instantiate internal serializers.

## What Each Argument Does

| Argument | Purpose |
|----------|---------|
| `--add-opens java.xml/com.sun.org.apache.xml.internal.serialize=ALL-UNNAMED` | Opens internal XML serialization classes (`XMLSerializer`, etc.) |
| `--add-opens java.xml/com.sun.org.apache.xpath.internal=ALL-UNNAMED` | Opens internal XPath implementation classes |
| `--add-opens java.base/java.lang=ALL-UNNAMED` | Opens core Java language reflection APIs |

## Impact

- **Unit Tests**: Already had proper configuration via `maven-surefire-plugin` in `xmlctf/pom.xml`
- **Integration Tests**: Now inherit these settings from Maven's JVM (via this `.mvn/jvm.config` file)
- **All Maven Plugins**: Any custom Maven plugin that runs tests or uses Castor XML will have access to these internal APIs

## Test Results

- **Before**: 489 tests run, 339 failures, 2 errors ❌
- **After**: 489 tests run, 0 failures, 0 errors ✅

## References

- Maven Documentation: https://maven.apache.org/configure.html
- JEP 403 (Strongly Encapsulate JDK Internals): https://openjdk.org/jeps/403
- Detailed Fix Analysis: See `../INTEGRATION_TESTS_FIX.md`

## Important Notes

⚠️ **Do NOT delete this file** - It is required for integration tests to pass on Java 9+

⚠️ **Do NOT delete the `.mvn` directory** - Maven requires this directory structure

✅ **Safe to commit** - This configuration is portable and works across all operating systems