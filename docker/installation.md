## Prerequisites

### Windows Subsystem for Linux (WSL)

To use the Docker CLI, we need to first install WSL on our Windows machine. You can follow the [Microsoft Tutorial for installing WSL](https://learn.microsoft.com/en-us/windows/wsl/install). The main steps are as follows:

1. You can now install everything you need to run WSL with a single command. Open PowerShell or Windows Command Prompt in administrator mode and run:
   ```ps
   wsl --install
   ```
    then restart your machine.

 The above command only works if WSL is not installed at all. If you run `wsl --install` and see the WSL help text, please try running `wsl --list --online` to see a list of available distros and `run wsl --install -d <DistroName>` to install a distro.

### Docker

The following is from the official [Docker installation tutorial](https://docs.docker.com/engine/install/ubuntu/#install-using-the-repository). Ensure you complete the following section using WSL:

Before you install Docker Engine for the first time on a new host machine, you need to set up the Docker apt repository. Afterward, you can install and update Docker from the repository.

1. Set up Docker's `apt` repository.
    ```bash
    # Add Docker's official GPG key:
    sudo apt-get update
    sudo apt-get install ca-certificates curl
    sudo install -m 0755 -d /etc/apt/keyrings
    sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
    sudo chmod a+r /etc/apt/keyrings/docker.asc

    # Add the repository to Apt sources:
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
      $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt-get update
    ```
2. Install the Docker packages.
    ```bash
    sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
    ```
3. Verify that the installation is successful by running the hello-world image:
   ```bash
   sudo docker run hello-world
   ```
    This command downloads a test image and runs it in a container. When the container runs, it prints a confirmation message and exits.

#### Docker Compose

There is a chance that you may also need to download Docker Compose which can be done using the following command:
```bash
sudo apt-get update
sudo apt-get install docker-compose-plugin
```
You can verify that Docker Compose is installed correctly by checking the version:
```bash
docker compose version
```
