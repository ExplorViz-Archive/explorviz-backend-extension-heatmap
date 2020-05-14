# ExplorViz-Backend-Heatmap-Extension

This repository holds the backend of the heatmap extension for the [ExplorViz Backend](https://github.com/ExplorViz/explorviz-backend). The extension adds a heatmap visualization of dynamic data to the ExplorViz landscape visualization of the frontend as described [here](https://github.com/ExplorViz/explorviz-frontend-extension-heatmap).

## Requirements
* [ExplorViz Backend](https://github.com/ExplorViz/explorviz-backend/tree/1.5.0) Version 1.5.0 
* [ExplorViz Frontend](https://github.com/ExplorViz/explorviz-frontend/tree/1.5.0) Version 1.5.0
* [ExplorViz Frontend Extension Heatmap](https://github.com/ExplorViz/explorviz-frontend-extension-heatmap)

## Setup (entire application)

To start the entire software stack of ExplirViz with our extension we provide a docker-compose file, which can be found [here](https://github.com/ExplorViz/explorviz-backend-extension-heatmap/blob/master/docker-compose/explorviz-extended/docker-compose.yml).

To start the application follow these steps:

1. Get the latest versions of Docker and Docker Compose.
2. Navigate into the directory of the docker-compose file.
3. Adjust the FRONTEND_IP variable to point to the domain or IP you will be accessing ExplorViz with. Depending on which machine you apply docker-compose, either use the machine's IP/hostname or localhost.
4. Starting ExplorViz: docker-compose up -d.
5. Open the frontend in your browser by accessing http://YOUR-IP:8090 ([http://localhost:8090](http://localhost:8090) by default).
6. Stopping ExplorViz: docker-compose down.

If you want to test a version with provided sample landscapes, that does not require a monitored application, use [this](https://github.com/ExplorViz/explorviz-backend-extension-heatmap/blob/master/docker-compose/explorviz-extended-sample/docker-compose.yml) docker-compose file instead.

## Setup (extension only)

1. Follow the [README](https://github.com/ExplorViz/explorviz-backend/tree/1.5.0/README.md) of the [ExplorViz Backend](https://github.com/ExplorViz/explorviz-backend/tree/1.5.0).
2. Clone this repository
3. Import project into eclipse: via `Import -> Gradle -> Existing Gradle project -> path/to/explorviz-backend-extension-heatmap`
4. Start the required MongoDB instance or use the docker-compose file [from below](#Attention)
5. Start explorviz-backend-extension-heatmap from eclipse
6. Setup and start [ExplorViz Frontend](https://github.com/ExplorViz/explorviz-frontend/tree/1.5.0) with the installed [ExplorViz Frontend Extension Heatmap](https://github.com/ExplorViz/explorviz-frontend-extension-heatmap) as explained in the frontend extension.

## Attention

For the communication between frontend and backend extension it is required  to add the backend routes of the broadcast and reload resources to the Traefik configuration. In [./docker-compose/traefik](https://github.com/ExplorViz/explorviz-backend-extension-heatmap/tree/master/docker-compose/traefik) we provide a configuration file that can be used.

Furthermore, we provide a [docker-compose file](https://github.com/ExplorViz/explorviz-backend-extension-heatmap/blob/master/docker-compose/auxiliary-software-stack/docker-compose.yml) to start the auxiliary services of the backend software stack alltogether.

If you want to use the backend separately you need to provide a MongoDB instance on port 27020 or configure the port in the [explorviz-properties](https://github.com/ExplorViz/explorviz-backend-extension-heatmap/blob/master/src/main/resources/explorviz.properties) of the extension.