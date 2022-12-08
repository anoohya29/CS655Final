#Script to be executed on worker node.
sudo apt update
# install java runtime environment
sudo apt install default-jre  
# install java compiler
sudo apt install default-jdk 
# download project files.
sudo wget --no-check-certificate --no-cache --no-cookies link  --post-data="action=purge"
# extract project files
sudo tar -zvxf password_cracker_v1.0.tar.gz 
# Change directory to worker folder.
cd CS655FinalProject/WorkerNode  # go into the worker node folder
# complie the java files
sudo javac WorkerNode.java
# start the worker
sudo java Worker 58100  