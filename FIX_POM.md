# Fix pom.xml

**IMPORTANT**: Line 18 of `pom.xml` has a typo that needs to be fixed manually.

## The Fix

Open `backend/pom.xml` and on line 18, change:

```xml
<n>AI Resume Analyzer</n>
```

to:

```xml
<name>AI Resume Analyzer</name>
```

That's it! Just add "ame" to make it `<name>` instead of `<n>`.

## Why This Happened

This was a simple typo in the XML tag. Maven requires the correct `<name>` tag for the project name.

