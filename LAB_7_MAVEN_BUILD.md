# Lab 7: Maven & Build Tools - Project Management and Build Automation

## Overview

Lab 7 focuses on Maven project management and build automation. This lab teaches how to configure Maven projects, manage dependencies, create build profiles, configure plugins, optimize builds, and automate testing and packaging. Students will learn enterprise-level build practices and project organization using Maven as the build tool.

## Learning Objectives

By completing this lab, you will understand:

- **Maven Project Structure:** How to organize projects following Maven conventions
- **Dependency Management:** How to declare and manage project dependencies
- **Build Lifecycle:** How Maven's build lifecycle works and what each phase does
- **Plugin Configuration:** How to configure and use Maven plugins
- **Build Profiles:** How to create different configurations for different environments
- **Testing Automation:** How to automate testing and code quality checks
- **Artifact Generation:** How to generate deployable artifacts (JAR, WAR)
- **Repository Management:** How to configure and use Maven repositories
- **Build Optimization:** How to optimize builds for performance
- **Multi-Module Projects:** How to organize large projects into modules
- **Properties and Variables:** How to use properties for configuration
- **Documentation and Reporting:** How to generate project documentation and reports

## Technology Stack

- **Maven:** 3.6+ (Build automation tool)
- **Java:** 21 (Programming language)
- **Spring Boot:** 3.2.0 (Framework)
- **JUnit 5:** 5.9.3 (Testing framework)
- **Plugins:**
  - maven-compiler-plugin: 3.11.0
  - maven-surefire-plugin: 3.1.2
  - maven-shade-plugin: 3.5.0
  - maven-jar-plugin: 3.3.0
  - jacoco-maven-plugin: 0.8.10
  - maven-site-plugin: 3.12.1

## Project Structure

```
project-root/
├── pom.xml                          # Project Object Model
├── src/
│   ├── main/
│   │   ├── java/com/bootcamp/onlineschool/
│   │   │   ├── OnlineSchoolApplication.java
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── controller/
│   │   │   └── dto/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-test.properties
│   │       └── application-prod.properties
│   └── test/
│       ├── java/com/bootcamp/onlineschool/
│       │   ├── entity/
│       │   ├── repository/
│       │   ├── service/
│       │   ├── controller/
│       │   └── integration/
│       └── resources/
│           └── application-test.properties
├── target/                          # Build output
│   ├── classes/                     # Compiled classes
│   ├── test-classes/                # Compiled tests
│   ├── *.jar                        # Generated artifacts
│   └── site/                        # Generated documentation
└── .m2/                             # Local Maven repository
```

## Key Concepts

### 1. Maven Build Lifecycle

Maven has three built-in build lifecycles:

**Default Lifecycle (most commonly used):**
1. **validate** - Validate project structure
2. **compile** - Compile source code
3. **test** - Run unit tests
4. **package** - Create JAR/WAR artifact
5. **verify** - Verify artifact quality
6. **install** - Install to local repository
7. **deploy** - Deploy to remote repository

**Clean Lifecycle:**
- **pre-clean** - Execute processes before clean
- **clean** - Remove previous build artifacts
- **post-clean** - Execute processes after clean

**Site Lifecycle:**
- **pre-site** - Execute processes before site generation
- **site** - Generate project documentation
- **post-site** - Execute processes after site generation
- **site-deploy** - Deploy generated site

### 2. POM Configuration

The `pom.xml` file is the heart of Maven:

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.bootcamp</groupId>
    <artifactId>online-school</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <name>Online School</name>
    <description>Online School Management System</description>
    
    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <!-- Dependencies here -->
    </dependencies>
    
    <build>
        <plugins>
            <!-- Plugins here -->
        </plugins>
    </build>
    
    <profiles>
        <!-- Build profiles here -->
    </profiles>
</project>
```

### 3. Dependency Management

Dependencies are declared in `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>3.2.0</version>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <version>3.2.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**Dependency Scopes:**
- **compile** - Available in all classpaths (default)
- **provided** - Available at compile time, provided by container
- **runtime** - Only needed at runtime
- **test** - Only needed for testing
- **system** - Similar to provided, from filesystem

### 4. Maven Plugins

Plugins extend Maven functionality:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>21</source>
                <target>21</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 5. Build Profiles

Profiles allow different configurations for different environments:

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <spring.profiles.active>dev</spring.profiles.active>
        </properties>
    </profile>
    
    <profile>
        <id>prod</id>
        <properties>
            <spring.profiles.active>prod</spring.profiles.active>
        </properties>
    </profile>
</profiles>
```

### 6. Properties and Variables

Properties centralize configuration:

```xml
<properties>
    <java.version>21</java.version>
    <spring-boot.version>3.2.0</spring-boot.version>
    <junit.version>5.9.3</junit.version>
</properties>

<!-- Reference properties with ${property.name} -->
<version>${spring-boot.version}</version>
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Git

### Running Maven Commands

```bash
# Clean previous build
mvn clean

# Compile source code
mvn compile

# Run unit tests
mvn test

# Package as JAR
mvn package

# Install to local repository
mvn install

# Deploy to remote repository
mvn deploy

# Generate documentation
mvn site

# Run with specific profile
mvn clean package -Pprod

# Run with verbose output
mvn clean package -X

# Skip tests
mvn clean package -DskipTests
```

### Building the Project

```bash
# Full build with all tests
mvn clean build

# Build without tests
mvn clean build -DskipTests

# Build with specific profile
mvn clean build -Pdev

# Build and install to local repository
mvn clean install

# Generate fat JAR with all dependencies
mvn clean package shade:shade
```

## Key Maven Plugins

### Compiler Plugin
Compiles Java source code with specified Java version.

### Surefire Plugin
Runs unit tests and generates test reports.

### Shade Plugin
Creates fat JAR with all dependencies included.

### JAR Plugin
Creates JAR artifact with manifest file.

### Site Plugin
Generates project documentation and reports.

### JaCoCo Plugin
Measures code coverage and generates coverage reports.

## Build Profiles

### Development Profile
- Debug logging enabled
- Development database configuration
- Verbose output
- Activated by default

### Test Profile
- Test database configuration
- Code coverage enabled
- Test-specific properties

### Production Profile
- Production logging level
- Production database configuration
- Performance optimizations
- Minimal output

## Common Tasks

### Creating a New Maven Project

```bash
mvn archetype:generate \
  -DgroupId=com.bootcamp \
  -DartifactId=online-school \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

### Adding a Dependency

1. Find the dependency on Maven Central (https://mvnrepository.com/)
2. Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.2.0</version>
</dependency>
```
3. Maven automatically downloads it

### Creating a Fat JAR

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentServiceTest

# Run specific test method
mvn test -Dtest=StudentServiceTest#testRegisterStudent

# Skip tests during build
mvn package -DskipTests
```

### Generating Reports

```bash
# Generate site with all reports
mvn site

# Generate only test reports
mvn surefire-report:report

# Generate code coverage report
mvn jacoco:report

# View reports
open target/site/index.html
```

## Troubleshooting

### Issue: "Could not find artifact"
**Solution:** Check dependency coordinates (groupId, artifactId, version) in pom.xml. Verify Maven Central has the artifact.

### Issue: "BUILD FAILURE - Tests failed"
**Solution:** Run tests individually to identify failing test. Fix the test or code. Run `mvn test` again.

### Issue: "Plugin not found"
**Solution:** Verify plugin coordinates in pom.xml. Check Maven Central for correct version.

### Issue: "Dependency conflict"
**Solution:** Use `mvn dependency:tree` to see dependency tree. Exclude conflicting transitive dependencies.

### Issue: "Out of memory during build"
**Solution:** Increase Maven heap size: `export MAVEN_OPTS="-Xmx1024m"`

### Issue: "Build is slow"
**Solution:** Enable parallel builds: `mvn -T 1C clean package` (1 thread per core)

## Best Practices

1. **Follow Maven conventions** - Use standard directory structure
2. **Use properties** - Centralize configuration in properties
3. **Manage versions** - Use dependency management for consistency
4. **Create profiles** - Different configurations for different environments
5. **Automate testing** - Run tests in build process
6. **Generate reports** - Use plugins to generate documentation
7. **Use meaningful names** - Clear groupId, artifactId, version
8. **Document dependencies** - Explain why each dependency is needed
9. **Keep pom.xml clean** - Remove unused dependencies
10. **Use build profiles** - Separate dev, test, prod configurations

## Lab Progression

This is **Lab 7** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals - Core Java and OOP ✅
- **Lab 2:** JUnit Testing - Advanced testing patterns ✅
- **Lab 3:** Spring Boot Basics - REST APIs and services ✅
- **Lab 4:** Database & SQL - SQL fundamentals (coming soon)
- **Lab 5:** ORM & JPA - Object-Relational Mapping (coming soon)
- **Lab 6:** Backend API - RESTful API development (coming soon)
- **Lab 7:** Maven & Build Tools - Build automation (current)
- **Lab 8:** Frontend HTML & CSS - Web basics (coming soon)
- **Lab 9:** ReactJS - Modern frontend (coming soon)
- **Lab 10:** Full Stack Integration - Complete application (coming soon)

## Switching Between Labs

```bash
# View all available branches
git branch -a

# Switch to Lab 7
git checkout lab/maven-7-build

# Switch to other labs
git checkout lab/java-1-fundamentals
git checkout lab/junit-2-testing
git checkout lab/springboot-3-basics
```

## Resources

- [Maven Official Documentation](https://maven.apache.org/)
- [Maven POM Reference](https://maven.apache.org/pom.html)
- [Maven Plugins](https://maven.apache.org/plugins/)
- [Maven Central Repository](https://mvnrepository.com/)
- [Spring Boot Maven Plugin](https://spring.io/guides/gs/maven/)
- [Maven Best Practices](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html)

## Next Steps

1. Review the requirements in `.kiro/specs/lab-7-maven-build/requirements.md`
2. Study the design in `.kiro/specs/lab-7-maven-build/design.md`
3. Execute tasks from `.kiro/specs/lab-7-maven-build/tasks.md`
4. Build the project: `mvn clean build`
5. Run tests: `mvn test`
6. Generate documentation: `mvn site`

---

**Last Updated:** November 21, 2025  
**Lab:** 7 - Maven & Build Tools  
**Status:** Specification Complete, Ready for Implementation
