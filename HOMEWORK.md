# Lab 7: Maven & Build Tools - Homework Assignment

## Overview

This homework assignment reinforces Maven project management and build automation concepts. You'll practice configuring Maven projects, managing dependencies, creating build profiles, configuring plugins, and automating builds.

## Learning Objectives

By completing this homework, you will:
- Configure Maven projects with proper POM structure
- Manage dependencies and resolve conflicts
- Create and use build profiles for different environments
- Configure Maven plugins for various tasks
- Automate testing and code quality checks
- Generate project documentation and reports
- Optimize build performance
- Understand Maven lifecycle and phases

## Prerequisites

- Completed Lab 7: Maven & Build Tools
- Understanding of Maven project structure
- Familiarity with pom.xml configuration
- Knowledge of Maven lifecycle and phases
- Basic understanding of build automation

---

## Assignment 1: Multi-Profile Configuration

### Task
Configure Maven profiles for development, testing, and production environments with different properties and plugin configurations.

### Requirements

1. **Create Development Profile** in `pom.xml`:
   - Profile ID: `dev`
   - Activated by default
   - Properties:
     - `spring.profiles.active=dev`
     - `logging.level=DEBUG`
     - `database.url=jdbc:h2:mem:devdb`
   - Enable verbose output
   - Skip integration tests

2. **Create Test Profile**:
   - Profile ID: `test`
   - Properties:
     - `spring.profiles.active=test`
     - `logging.level=INFO`
     - `database.url=jdbc:h2:mem:testdb`
   - Enable code coverage with JaCoCo
   - Run all tests including integration tests
   - Generate test reports

3. **Create Production Profile**:
   - Profile ID: `prod`
   - Properties:
     - `spring.profiles.active=prod`
     - `logging.level=WARN`
     - `database.url=${env.DATABASE_URL}`
   - Optimize for performance
   - Skip tests (assume CI/CD ran them)
   - Create fat JAR with all dependencies

4. **Create corresponding property files**:
   - `src/main/resources/application-dev.properties`
   - `src/main/resources/application-test.properties`
   - `src/main/resources/application-prod.properties`
   - Each with environment-specific configuration

5. **Test profile activation**:
   - Build with dev profile: `mvn clean package`
   - Build with test profile: `mvn clean package -Ptest`
   - Build with prod profile: `mvn clean package -Pprod`
   - Verify correct properties are used in each build

### Expected Deliverables
- Updated pom.xml with three profiles
- Three application property files
- Documentation explaining each profile's purpose
- Build commands for each profile

---

## Assignment 2: Plugin Configuration and Automation

### Task
Configure Maven plugins for compilation, testing, packaging, and code quality.

### Requirements

1. **Configure Compiler Plugin**:
   - Set Java version to 21
   - Enable all warnings
   - Treat warnings as errors (optional)
   - Configure annotation processing
   - Set encoding to UTF-8

2. **Configure Surefire Plugin** (Unit Tests):
   - Run tests in parallel (2 threads)
   - Generate XML and HTML reports
   - Exclude integration tests (`**/*IntegrationTest.java`)
   - Configure test output verbosity
   - Set memory options for tests

3. **Configure Failsafe Plugin** (Integration Tests):
   - Include integration tests (`**/*IntegrationTest.java`)
   - Run in separate phase (integration-test)
   - Generate separate reports
   - Configure test database setup/teardown

4. **Configure JaCoCo Plugin** (Code Coverage):
   - Prepare agent for test execution
   - Generate coverage report after tests
   - Set minimum coverage thresholds:
     - Line coverage: 80%
     - Branch coverage: 70%
     - Class coverage: 90%
   - Fail build if thresholds not met
   - Exclude certain packages from coverage

5. **Configure Shade Plugin** (Fat JAR):
   - Create executable JAR with all dependencies
   - Set main class in manifest
   - Relocate conflicting dependencies
   - Minimize JAR size by removing unused classes
   - Create separate classifier for fat JAR

6. **Configure JAR Plugin**:
   - Add manifest entries:
     - Implementation-Title
     - Implementation-Version
     - Built-By
     - Build-Time
   - Exclude test resources

7. **Configure Site Plugin**:
   - Generate project documentation
   - Include test reports
   - Include code coverage reports
   - Include dependency reports
   - Configure site skin/theme

### Expected Deliverables
- Updated pom.xml with all plugin configurations
- Successful build with all plugins working
- Generated reports in target/site
- Fat JAR in target directory
- Documentation explaining each plugin's purpose

---

## Assignment 3: Dependency Management

### Task
Properly manage project dependencies, handle conflicts, and optimize dependency tree.

### Requirements

1. **Add Required Dependencies**:
   - Spring Boot Starter Web
   - Spring Boot Starter Data JPA
   - Spring Boot Starter Validation
   - H2 Database (runtime scope)
   - Lombok (provided scope)
   - Spring Boot Starter Test (test scope)

2. **Use Dependency Management Section**:
   - Create `<dependencyManagement>` section
   - Define versions for all Spring Boot dependencies
   - Use Spring Boot BOM (Bill of Materials)
   - Centralize version management

3. **Configure Properties for Versions**:
   - Create properties for all dependency versions
   - Use properties in dependency declarations
   - Example: `<spring-boot.version>3.2.0</spring-boot.version>`

4. **Handle Dependency Conflicts**:
   - Run `mvn dependency:tree` to view dependency tree
   - Identify any conflicting transitive dependencies
   - Use `<exclusions>` to exclude unwanted versions
   - Document why exclusions are needed

5. **Optimize Dependencies**:
   - Run `mvn dependency:analyze` to find:
     - Used but undeclared dependencies
     - Declared but unused dependencies
   - Remove unused dependencies
   - Add missing dependencies
   - Document all dependencies with comments

6. **Create Dependency Report**:
   - Generate dependency report: `mvn dependency:tree > dependency-tree.txt`
   - Generate dependency analysis: `mvn dependency:analyze > dependency-analysis.txt`
   - Review and document findings

### Expected Deliverables
- Updated pom.xml with proper dependency management
- Properties section with version variables
- dependency-tree.txt file
- dependency-analysis.txt file
- Documentation explaining dependency choices

---

## Assignment 4: Build Optimization and Automation

### Task
Optimize build performance and create automated build scripts.

### Requirements

1. **Enable Parallel Builds**:
   - Configure Maven to use multiple threads
   - Test with: `mvn clean package -T 1C` (1 thread per CPU core)
   - Measure and document build time improvement

2. **Configure Build Caching**:
   - Enable incremental compilation
   - Configure Maven to skip unchanged modules
   - Use `mvn compile` to test incremental builds

3. **Create Build Scripts**:
   - Create `build-dev.sh` (or .bat for Windows):
     - Clean build with dev profile
     - Run unit tests
     - Generate reports
   - Create `build-test.sh`:
     - Clean build with test profile
     - Run all tests including integration tests
     - Generate coverage reports
     - Fail if coverage below threshold
   - Create `build-prod.sh`:
     - Clean build with prod profile
     - Skip tests
     - Create fat JAR
     - Copy JAR to deployment directory
   - Make scripts executable: `chmod +x *.sh`

4. **Configure Maven Wrapper**:
   - Generate Maven wrapper: `mvn wrapper:wrapper`
   - Set Maven version in `.mvn/wrapper/maven-wrapper.properties`
   - Update scripts to use `./mvnw` instead of `mvn`
   - Test wrapper works without Maven installed

5. **Create CI/CD Configuration** (optional):
   - Create `.github/workflows/build.yml` for GitHub Actions
   - Or create `Jenkinsfile` for Jenkins
   - Configure automated builds on push
   - Run tests and generate reports
   - Archive build artifacts

6. **Measure Build Performance**:
   - Document build times for:
     - Clean build
     - Incremental build
     - Parallel build
     - Single-threaded build
   - Create comparison table

### Expected Deliverables
- Build scripts (build-dev.sh, build-test.sh, build-prod.sh)
- Maven wrapper files (.mvn directory, mvnw, mvnw.cmd)
- CI/CD configuration file (optional)
- Build performance documentation
- README with build instructions

---

## Bonus Challenges (Optional)

### Challenge 1: Multi-Module Project
Create a multi-module Maven project:
- Parent POM with common configuration
- Module 1: Core domain models
- Module 2: Data access layer
- Module 3: Service layer
- Module 4: REST API layer
- Configure inter-module dependencies
- Build all modules with single command

### Challenge 2: Custom Maven Plugin
Create a simple custom Maven plugin:
- Generate build timestamp file
- Validate project structure
- Generate custom reports
- Package and install plugin locally
- Use plugin in main project

### Challenge 3: Release Management
Configure Maven Release Plugin:
- Set up version numbering
- Configure SCM (Git) integration
- Create release preparation
- Tag releases in Git
- Deploy to Maven repository

### Challenge 4: Advanced Reporting
Configure additional reporting plugins:
- Checkstyle for code style
- PMD for code quality
- SpotBugs for bug detection
- Maven Project Info Reports
- Aggregate all reports in site

### Challenge 5: Docker Integration
Integrate Docker with Maven build:
- Use Jib Maven Plugin
- Build Docker image during package phase
- Configure image tags and labels
- Push to Docker registry
- Create docker-compose.yml for local testing

---

## Running Your Builds

### Basic Maven Commands

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

# Run with specific profile
mvn clean package -Pdev
mvn clean package -Ptest
mvn clean package -Pprod

# Skip tests
mvn clean package -DskipTests

# Run specific test
mvn test -Dtest=StudentServiceTest

# Generate site documentation
mvn site

# View dependency tree
mvn dependency:tree

# Analyze dependencies
mvn dependency:analyze

# Parallel build
mvn clean package -T 1C

# Verbose output
mvn clean package -X

# Quiet output
mvn clean package -q
```

### Using Build Scripts

```bash
# Make scripts executable
chmod +x build-*.sh

# Run development build
./build-dev.sh

# Run test build
./build-test.sh

# Run production build
./build-prod.sh

# Using Maven wrapper
./mvnw clean package
```

### Viewing Reports

```bash
# Generate all reports
mvn clean test site

# Open site in browser
open target/site/index.html

# View test reports
open target/surefire-reports/index.html

# View coverage report
open target/site/jacoco/index.html
```

---

## Submission Checklist

Before submitting, ensure you have:

- [ ] Updated pom.xml with all configurations
- [ ] Three build profiles (dev, test, prod)
- [ ] Three application property files
- [ ] All plugins properly configured
- [ ] Dependency management section
- [ ] Properties for version management
- [ ] Build scripts for each profile
- [ ] Maven wrapper configured
- [ ] All builds successful
- [ ] Tests passing
- [ ] Reports generated
- [ ] Documentation complete
- [ ] dependency-tree.txt file
- [ ] dependency-analysis.txt file
- [ ] Build performance measurements
- [ ] README with build instructions

### Expected Build Results

```bash
# Development build
./build-dev.sh
[INFO] BUILD SUCCESS
[INFO] Total time: 15.234 s

# Test build with coverage
./build-test.sh
[INFO] BUILD SUCCESS
[INFO] Coverage: 85% (threshold: 80%)

# Production build
./build-prod.sh
[INFO] BUILD SUCCESS
[INFO] JAR created: target/online-school-1.0.0.jar
```

---

## Code Quality Guidelines

### POM Structure Best Practices

1. **Well-Organized POM**
   ```xml
   <project>
       <!-- Project coordinates -->
       <modelVersion>4.0.0</modelVersion>
       <groupId>com.bootcamp</groupId>
       <artifactId>online-school</artifactId>
       <version>1.0.0</version>
       <packaging>jar</packaging>
       
       <!-- Project information -->
       <name>Online School</name>
       <description>Online School Management System</description>
       
       <!-- Properties -->
       <properties>
           <java.version>21</java.version>
           <spring-boot.version>3.2.0</spring-boot.version>
           <maven.compiler.source>21</maven.compiler.source>
           <maven.compiler.target>21</maven.compiler.target>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
       </properties>
       
       <!-- Dependency Management -->
       <dependencyManagement>
           <!-- Centralized version management -->
       </dependencyManagement>
       
       <!-- Dependencies -->
       <dependencies>
           <!-- Actual dependencies -->
       </dependencies>
       
       <!-- Build Configuration -->
       <build>
           <plugins>
               <!-- Plugin configurations -->
           </plugins>
       </build>
       
       <!-- Profiles -->
       <profiles>
           <!-- Build profiles -->
       </profiles>
   </project>
   ```

2. **Profile Configuration**
   ```xml
   <profiles>
       <profile>
           <id>dev</id>
           <activation>
               <activeByDefault>true</activeByDefault>
           </activation>
           <properties>
               <spring.profiles.active>dev</spring.profiles.active>
               <logging.level>DEBUG</logging.level>
           </properties>
           <build>
               <plugins>
                   <!-- Dev-specific plugins -->
               </plugins>
           </build>
       </profile>
       
       <profile>
           <id>prod</id>
           <properties>
               <spring.profiles.active>prod</spring.profiles.active>
               <logging.level>WARN</logging.level>
           </properties>
           <build>
               <plugins>
                   <!-- Prod-specific plugins -->
                   <plugin>
                       <groupId>org.apache.maven.plugins</groupId>
                       <artifactId>maven-shade-plugin</artifactId>
                       <executions>
                           <execution>
                               <phase>package</phase>
                               <goals>
                                   <goal>shade</goal>
                               </goals>
                           </execution>
                       </executions>
                   </plugin>
               </plugins>
           </build>
       </profile>
   </profiles>
   ```

3. **Plugin Configuration**
   ```xml
   <build>
       <plugins>
           <!-- Compiler Plugin -->
           <plugin>
               <groupId>org.apache.maven.plugins</groupId>
               <artifactId>maven-compiler-plugin</artifactId>
               <version>3.11.0</version>
               <configuration>
                   <source>${java.version}</source>
                   <target>${java.version}</target>
                   <encoding>${project.build.sourceEncoding}</encoding>
                   <showWarnings>true</showWarnings>
                   <showDeprecation>true</showDeprecation>
               </configuration>
           </plugin>
           
           <!-- Surefire Plugin -->
           <plugin>
               <groupId>org.apache.maven.plugins</groupId>
               <artifactId>maven-surefire-plugin</artifactId>
               <version>3.1.2</version>
               <configuration>
                   <parallel>methods</parallel>
                   <threadCount>2</threadCount>
                   <excludes>
                       <exclude>**/*IntegrationTest.java</exclude>
                   </excludes>
               </configuration>
           </plugin>
           
           <!-- JaCoCo Plugin -->
           <plugin>
               <groupId>org.jacoco</groupId>
               <artifactId>jacoco-maven-plugin</artifactId>
               <version>0.8.10</version>
               <executions>
                   <execution>
                       <goals>
                           <goal>prepare-agent</goal>
                       </goals>
                   </execution>
                   <execution>
                       <id>report</id>
                       <phase>test</phase>
                       <goals>
                           <goal>report</goal>
                       </goals>
                   </execution>
                   <execution>
                       <id>check</id>
                       <goals>
                           <goal>check</goal>
                       </goals>
                       <configuration>
                           <rules>
                               <rule>
                                   <element>BUNDLE</element>
                                   <limits>
                                       <limit>
                                           <counter>LINE</counter>
                                           <value>COVEREDRATIO</value>
                                           <minimum>0.80</minimum>
                                       </limit>
                                   </limits>
                               </rule>
                           </rules>
                       </configuration>
                   </execution>
               </executions>
           </plugin>
       </plugins>
   </build>
   ```

4. **Build Script Example**
   ```bash
   #!/bin/bash
   # build-test.sh - Test build with coverage
   
   echo "Starting test build..."
   
   # Clean previous build
   ./mvnw clean
   
   # Run build with test profile
   ./mvnw package -Ptest
   
   # Check exit code
   if [ $? -eq 0 ]; then
       echo "Build successful!"
       echo "Reports available at: target/site/index.html"
   else
       echo "Build failed!"
       exit 1
   fi
   ```

### Maven Best Practices
- Use properties for all version numbers
- Group related dependencies together
- Add comments explaining dependency purposes
- Use dependency management for version consistency
- Configure plugins explicitly (don't rely on defaults)
- Use profiles for environment-specific configuration
- Keep POM organized and readable
- Document custom configurations
- Use Maven wrapper for reproducible builds
- Optimize build performance with parallel execution

---

## Common Pitfalls to Avoid

- ❌ Hardcoding version numbers in dependencies
- ❌ Not using dependency management
- ❌ Mixing compile and test dependencies
- ❌ Not excluding transitive dependencies when needed
- ❌ Forgetting to configure plugin versions
- ❌ Not using properties for configuration
- ❌ Creating profiles that are too similar
- ❌ Not testing builds with different profiles
- ❌ Committing target directory to Git
- ❌ Not using Maven wrapper
- ❌ Ignoring dependency:analyze warnings
- ❌ Not documenting custom configurations

---

## Tips for Success

1. **Start with properties** - Define all versions as properties first
2. **Test each profile** - Build with each profile to verify configuration
3. **Use dependency:tree** - Understand your dependency graph
4. **Read plugin documentation** - Understand what each plugin does
5. **Measure build times** - Optimize based on measurements
6. **Use Maven wrapper** - Ensure consistent builds across environments
7. **Document everything** - Explain why configurations exist
8. **Keep POM organized** - Follow standard structure
9. **Test incrementally** - Add one plugin at a time
10. **Use build scripts** - Automate common build tasks

---

## Resources

### Documentation
- [Maven Official Documentation](https://maven.apache.org/)
- [Maven POM Reference](https://maven.apache.org/pom.html)
- [Maven Plugins](https://maven.apache.org/plugins/)
- [Maven Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
- [Maven Profiles](https://maven.apache.org/guides/introduction/introduction-to-profiles.html)
- Lab 7 LAB_7_MAVEN_BUILD.md - Review Maven concepts

### Plugin Documentation
- [Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)
- [Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/)
- [JaCoCo Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [Site Plugin](https://maven.apache.org/plugins/maven-site-plugin/)

### Tools
- [Maven Central Repository](https://mvnrepository.com/)
- [Maven Wrapper](https://github.com/takari/maven-wrapper)

---

## Questions and Support

If you encounter issues:

1. **Check Maven version** - Run `mvn -version`
2. **Clean and rebuild** - Run `mvn clean` then rebuild
3. **Check for typos** - Verify groupId, artifactId, version
4. **View dependency tree** - Run `mvn dependency:tree`
5. **Enable debug output** - Run with `-X` flag
6. **Check plugin versions** - Ensure compatible versions
7. **Read error messages** - Maven provides detailed errors
8. **Ask for help** - Reach out during office hours

---

**Remember: Mastering Maven is essential for professional Java development. These build automation skills will serve you throughout your career!** 🚀
