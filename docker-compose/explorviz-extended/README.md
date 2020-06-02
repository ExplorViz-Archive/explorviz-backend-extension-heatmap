## Usage

To start the application follow these steps:

1. Get the latest versions of [Docker and Docker Compose](https://www.docker.com/get-started).
2. Navigate into the directory of the docker-compose file.
3. Adjust the `FRONTEND_IP` variable to point to the domain or IP you will be accessing ExplorViz with. Depending on which machine you apply docker-compose, either use the machine's IP/hostname or localhost.
4. Starting ExplorViz: `docker-compose up -d`.
5. Open the frontend in your browser by accessing http://YOUR-IP:8090 ([http://localhost:8090](http://localhost:8090) by default).
6. Stopping ExplorViz: `docker-compose down`.
