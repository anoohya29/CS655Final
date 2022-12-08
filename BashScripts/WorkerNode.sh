#Script to be executed on worker node.
sudo apt update
# install java runtime environment
sudo apt install default-jre  
# install java compiler
sudo apt install default-jdk 
# download project files.
sudo wget --no-check-certificate --no-cache --no-cookies https://github.com/anoohya29/CS655Final/blob/e16db664b63f50f17b155b36f728c5c0ab4722c2/CS655FinalProject.tar.gz
# extract project files
sudo tar -zvxf CS655FinalProject.tar.gz
# Change directory to worker folder.
cd CS655FinalProject/WorkerNode  # go into the worker node folder
# complie the java files
sudo javac WorkerNode.java
# start the worker
sudo java Worker 58100  