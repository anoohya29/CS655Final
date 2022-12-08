#Script to be executed on manager node.
sudo apt update
# install java runtime environment
sudo apt install default-jre  
# install java compiler
sudo apt install default-jdk 
# we need to insatll nginx
sudo apt install nginx
# download project files.
sudo wget --no-check-certificate --no-cache --no-cookies https://github.com/anoohya29/CS655Final/blob/e16db664b63f50f17b155b36f728c5c0ab4722c2/CS655FinalProject.tar.gz
# extract project files
sudo tar -zvxf CS655FinalProject.tar.gz
# create the configuration file for nginx
sudo touch /etc/nginx/conf.d/static-naice-me.conf
# start writing configuration for nginx
sudo echo 'server {' > /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  server_name pcvm2-9.instageni.utdallas.edu;' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  root '`pwd`'/CS655FinalProject/Web;' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  index index.html;' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  location ~* ^.+\.(jpg|jpeg|gif|png|ico|css|js|pdf|txt){' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '    root '`pwd`'/CS655FinalProject/Web;' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  }' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '}' >> /etc/nginx/conf.d/static-naice-me.conf

# restart the nginx
sudo nginx -s reload  
# Change directory to manager folder.
cd CS655FinalProject/ManagerNode
# complie the java files
sudo javac NodeManager.java
# start the manager
sudo java NodeManager 58888 info
