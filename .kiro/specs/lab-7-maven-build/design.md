# Lab 7: Maven & Build Tools - Design Document

## Overview

Lab 7 implements comprehensive Maven project management and build automation. The design focuses on configuring Maven projects, managing dependencies, creating build profiles, configuring plugins, optimizing builds, and automating testing and packaging. The lab teaches enterprise-level build practices using Maven as the primary build tool for the online school system.

## Architecture

### Maven Build Lifecycle

```
┌─────────────────────────────────────────┐
│         Maven Lifecycle Phases          │
├─────────────────────────────────────────┤
│ 1. validate      - Validate project     │
│ 2. compile       - Compile source code  │
│ 3. test          - Run unit tests       │
│ 4. package       - Create artifact      │
│ 5. verify        - Verify artifact      │
│ 6. install       - Install to local repo│
│ 7. deploy        - Deploy to remote repo│
└─────────────────────────────────────────┘
```

### Maven Project Structure

```
project-root/
├── pom.xml                          # Project Object Model
├── src/
│   ├── main/
│   │   ├── java/                    # Source code
│   │   ├── resources/               # Configuration files
│   │   └── webapp/                  # Web resources (if WAR)
│   └── test/
│       ├── java/                    # Test code
│       └── resources/               # Test resources
├── target/                          # Build output
│   ├── classes/                     # Compiled classes
│   ├── test-classes/                # Compiled tests
│   └── *.jar                        # Generated artifact
└── .m2/                             # Local Maven repository
```

### Build Process Flow

```
┌─────────────────┐
│  Source Code    │
└────────┬────────┘
         │
    ┌────▼────────────────────┐
    │  Compile Phase          │
    │  - Compile Java files   │
    │  - Copy resources       │
    └────┬───────────────────┘
         │
    ┌────▼────────────────────┐
    │  Test Phase             │
    │  - Compile tests        │
    │  - Run unit tests       │
    │  - Generate reports     │
    └────┬───────────────────┘
         │
    ┌────▼────────────────────┐
    │  Package Phase          │
    │  - Create JAR/WAR       │
    │  - Add manifest         │
    │  - Sign artifact        │
    └────┬───────────────────┘
         │
    ┌────▼────────────────────┐
    │  Verify Phase           │
    │  - Run integration tests│
    │  - Check quality        │
    └────┬───────────────────┘
         │
    ┌────▼────────────────────┐
    │  Install Phase          │
    │  - Copy to local repo   │
    └────┬───────────────────┘
         │
    ┌────▼────────────────────┐
    │  Deploy Phase           │
    │  - Upload to remote repo│
    └────────────────────────┘
```

## Components and Interfaces

### POM Configuration Structure

#### Parent POM (pom.xml)
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
        <!-- Dependencies -->
    </dependencies>
    
    <build>
        <plugins>
            <!-- Plugins -->
        </plugins>
    </build>
    
    <profiles>
        <!-- Build Profiles -->
    </profiles>
</project>
```

### Key Maven Plugins

#### Compiler Plugin
- Compiles Java source code
- Configures Java version
- Sets compiler options

#### Surefire Plugin
- Runs unit tests
- Generates test reports
- Supports test filtering

#### Failsafe Plugin
- Runs integration tests
- Separate from unit tests
- Generates integration test reports

#### Assembly Plugin
- Creates fat JAR with dependencies
- Packages application for distribution
- Supports multiple formats

#### Shade Plugin
- Creates uber JAR
- Relocates classes to avoid conflicts
- Includes all dependencies

#### JAR Plugin
- Creates JAR artifact
- Adds manifest file
- Configures main class

#### WAR Plugin
- Creates WAR artifact
- Packages web resources
- Configures deployment descriptor

#### Surefire Report Plugin
- Generates test reports
- Creates HTML reports
- Aggregates test results

#### Jacoco Plugin
- Measures code coverage
- Generates coverage reports
- Enforces coverage thresholds

## Data Models

### POM Structure

```
Project
├── ModelVersion: 4.0.0
├── GroupId: com.bootcamp
├── ArtifactId: online-school
├── Version: 1.0.0
├── Packaging: jar
├── Name: Online School
├── Description: Online School Management System
├── Properties
│   ├── java.version: 21
│   ├── maven.compiler.source: 21
│   └── maven.compiler.target: 21
├── Dependencies
│   ├── Dependency 1
│   ├── Dependency 2
│   └── ...
├── Build
│   ├── Plugins
│   │   ├── Plugin 1
│   │   ├── Plugin 2
│   │   └── ...
│   └── Resources
├── Profiles
│   ├── Profile 1 (dev)
│   ├── Profile 2 (test)
│   └── Profile 3 (prod)
└── Repositories
    ├── Repository 1
    └── Repository 2
```

### Dependency Scope

| Scope | Compile | Runtime | Test | Description |
|-------|---------|---------|------|-------------|
| compile | ✓ | ✓ | ✓ | Default scope, included in all classpaths |
| provided | ✓ | ✗ | ✓ | Provided by container, not packaged |
| runtime | ✗ | ✓ | ✓ | Only needed at runtime |
| test | ✗ | ✗ | ✓ | Only needed for testing |
| system | ✓ | ✓ | ✓ | Similar to provided, from filesystem |
| import | ✗ | ✗ | ✗ | For importing BOM dependencies |

## Correctness Properties

A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.

### Property 1: Maven Project Structure Compliance
*For any* Maven project, the directory structure should follow Maven conventions (src/main/java, src/test/java, target, etc.) and Maven should recognize it without additional configuration.

**Validates: Requirements 1.1, 1.4**

### Property 2: Dependency Resolution Completeness
*For any* set of declared dependencies in pom.xml, Maven should download all dependencies and transitive dependencies, and they should be available in the project classpath.

**Validates: Requirements 2.1, 2.2, 2.3**

### Property 3: Build Lifecycle Phase Execution
*For any* Maven build phase executed, the system should execute all preceding phases in the correct order and produce expected outputs.

**Validates: Requirements 3.1, 3.2, 3.3, 3.4, 3.5**

### Property 4: Plugin Configuration Application
*For any* plugin configured in pom.xml, the plugin should execute during the appropriate build phase and apply the specified configuration.

**Validates: Requirements 4.1, 4.2, 4.3, 4.4**

### Property 5: Build Profile Activation
*For any* build profile defined, when activated (explicitly or by default), the profile-specific configuration should be applied and override default configuration.

**Validates: Requirements 5.1, 5.2, 5.3, 5.4, 5.5**

### Property 6: Test Execution and Reporting
*For any* unit test executed via mvn test, all tests should run and results should be reported, with build failing if any test fails.

**Validates: Requirements 6.1, 6.2, 6.3**

### Property 7: Artifact Generation Correctness
*For any* mvn package execution, a JAR artifact should be created in the target directory with correct naming and manifest file.

**Validates: Requirements 7.1, 7.2, 7.3, 7.4, 7.5**

### Property 8: Repository Configuration Effectiveness
*For any* repository configured, Maven should retrieve dependencies from that repository and use configured credentials if required.

**Validates: Requirements 8.1, 8.2, 8.3, 8.4, 8.5**

### Property 9: Build Performance Optimization
*For any* build executed with optimization enabled, the build should complete faster than without optimization while producing identical results.

**Validates: Requirements 9.1, 9.2, 9.3, 9.4, 9.5**

### Property 10: Multi-Module Build Order
*For any* multi-module project, modules should be built in correct dependency order, with dependent modules built after their dependencies.

**Validates: Requirements 10.1, 10.2, 10.3, 10.4, 10.5**

### Property 11: Property Substitution Consistency
*For any* property defined in pom.xml, all references to that property should be substituted with the same value consistently.

**Validates: Requirements 11.1, 11.2, 11.3, 11.4, 11.5**

### Property 12: Documentation Generation Completeness
*For any* mvn site execution, project documentation should be generated with all configured reports included and accessible via HTML.

**Validates: Requirements 12.1, 12.2, 12.3, 12.4, 12.5**

## Error Handling

### Build Failures

- **Compilation Error:** Source code has syntax errors
- **Test Failure:** Unit test assertion failed
- **Dependency Resolution Error:** Dependency not found in repositories
- **Plugin Execution Error:** Plugin failed during execution
- **Configuration Error:** Invalid pom.xml configuration

### Error Messages

- Clear indication of which phase failed
- Specific error details and stack trace
- Suggestions for resolution
- Links to relevant documentation

## Testing Strategy

### Unit Testing Approach
- Test Maven configuration parsing
- Test dependency resolution logic
- Test plugin execution
- Test build phase ordering

### Integration Testing Approach
- Test complete build lifecycle
- Test multi-module builds
- Test profile activation
- Test artifact generation

### Build Verification Approach
- Verify project structure
- Verify dependencies are resolved
- Verify artifacts are generated
- Verify tests pass
- Verify code quality

### Test Organization
```
src/test/java/com/bootcamp/onlineschool/
├── build/
│   ├── MavenConfigTest.java
│   ├── DependencyResolutionTest.java
│   └── PluginExecutionTest.java
└── integration/
    └── BuildLifecycleTest.java
```

### Test Coverage Goals
- **Configuration Tests:** 10+ tests covering pom.xml configuration
- **Build Tests:** 15+ tests covering build lifecycle
- **Plugin Tests:** 10+ tests covering plugin execution
- **Integration Tests:** 10+ tests covering complete workflows
- **Property-Based Tests:** 12 properties with 100+ iterations each
- **Total:** 55+ tests with comprehensive coverage

### Testing Best Practices
- Test with clean Maven repository
- Verify build artifacts are generated
- Check build output and logs
- Verify dependencies are included
- Test error scenarios
