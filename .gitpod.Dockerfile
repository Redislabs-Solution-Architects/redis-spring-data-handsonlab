FROM gitpod/workspace-full
CMD sdk install java 17.0.1.12.1-amzn < /dev/null
CMD sudo apt-get install redis-server
