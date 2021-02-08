# Mental Health Data Access

This is the Mental Health Data Access desktop application. It is
implemented as a Java Swing application using Stanford CoreNLP and Microsoft
Access.

The overall idea is to provide an application where patients can describe the
mental health issues they are facing with a natural language description. The
application then follows up with a series of questions while trying to narrow
down possible diagnoses. Once a certain threshold is reached, the program
displays a list of the most probable diagnoses (obfuscated behind numerical
codes to prevent the risk of self-diagnosis), which the patient can then take
to a human mental health professional for follow-up.

Doctors can also use the application to store session information from
previous patients and track progress over time after subsequent uses.

## Setup for development

### Configure javaFX

Currently this porject is step through replacing Swing with javaFX. You have to download the javaFX SDK so that you could run this project. Please download 11.0.2 version on https://gluonhq.com/products/javafx/

Second, you have to set up PATH environment variable for the javafx; please checkout this website to see how to set the path https://taylorial.com/cs1021/Install.htm

### IDE

This project requires Java 11 or above to run. You should be able to use any
compatible IDE of your choice to edit and run the project.

### Checking out the project

This project uses [Git][Git] for source control and is currently
[hosted on GitLab][GitLab repository overview] in a private repository.
You will need a Git client installed on your computer in order to check out
the repository and work with it. An existing project member will also have to
send you an invite to grant access to the repository.

See this [project wiki page][Wiki page for Git] for a quick guide to working
with Git on this project.

### Build and run the project

This project uses [Gradle][Gradle]. If you don't have Gradle already on your
system, don't worry: this project also includes the Gradle Wrapper to handle
first-time setup for you.

If you're on Windows, use the following command to build the project while
in the project root in a `cmd.exe` or PowerShell window (if you're on
macOS or Linus, use `./gradlew` instead):

```PS
.\gradlew.bat clean build run
```

### Logging Into the Application
Once you build and run the application you will be prompted with a User Login
window where you need to enter a username and password. The default login is:

1. Username: admin
1. Password: password

Click the login button and you will be presented with the DesktopGUI. 

### Running unit tests

You can also run all unit tests in the project by doing:

```PS
.\gradlew.bat clean build test
```

### Running Gradle tasks in an IDE

If you prefer, you can use your IDE's GUI to run the Gradle tasks above instead
of using the command line. Some common IDEs' instructions are in the links
below:

- [IntelliJ IDEA][Gradle in IntelliJ]
- [Visual Studio Code][Gradle in VS Code]
- [Eclipse][Gradle in Eclipse]


[Apache Maven]: https://maven.apache.org/index.html
[Apache Maven binary]: https://maven.apache.org/guides/getting-started/windows-prerequisites.html
[Chocolatey]: https://chocolatey.org/
[Git]: https://git-scm.com/
[GitLab repository overview]: https://gitlab.com/ltu-dsm-project/mental-health-data-access
[Gradle]: https://gradle.org/
[Gradle in Eclipse]: https://www.eclipse.org/community/eclipse_newsletter/2018/february/buildship.php
[Gradle in IntelliJ]: https://www.jetbrains.com/help/idea/work-with-gradle-tasks.html
[Gradle in VS Code]: https://marketplace.visualstudio.com/items?itemName=richardwillis.vscode-gradle
[Maven Chocolatey package]: https://chocolatey.org/packages/maven
[Wiki page for Git]: https://gitlab.com/ltu-dsm-project/mental-health-data-access/-/wikis/Checking-out-the-project-from-Git
