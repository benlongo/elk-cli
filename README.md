# Configuring graal

## Windows

run `installDist` from gradle

Run the Native Tools Command Prompt and execute 
```cmd
set JAVA_OPTS=-agentlib:native-image-agent=config-output-dir=build/classes/META-INF/native-image
build\install\elk-cli\bin\elk-cli
```
Paste the input, press Enter -> Ctrl Z -> Enter  for EOF.