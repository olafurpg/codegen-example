lazy val Syntax = RootProject(
  uri(
    "git://github.com/olafurpg/scala-syntax.git#9dc1da7e19991bfc95e6a874de48eb5b95e4a3a5"
  )
)
lazy val syntax = ProjectRef(Syntax.build, "format")

lazy val codegen = project.dependsOn(syntax)
