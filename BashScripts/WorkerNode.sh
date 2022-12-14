#Script to be executed on worker node.
sudo apt update
# install java runtime environment
sudo apt install default-jre  
# install java compiler
sudo apt install default-jdk
# delete old files
sudo rm main.zip
sudo rm -r CS655Final-main
# download project files.
sudo wget --no-check-certificate --no-cache --no-cookies https://github.com/anoohya29/CS655Final/archive/refs/heads/main.zip
# extract project files
sudo unzip main.zip
# Change directory to worker folder.
cd CS655Final-main/WorkerNode  # go into the worker node folder
# complie the java files
sudo javac WorkerNode.java
# start the worker
sudo java WorkerNode 58100