# ExplorViz-Backend-Heatmap-Extension

This repository holds the backend of the heatmap extension for the [ExplorViz Backend](https://github.com/ExplorViz/explorviz-backend). The extension adds a heatmap visualization of dynamic data to the ExplorViz landscape visualization of the frontend as described [here](https://github.com/ExplorViz/explorviz-frontend-extension-heatmap).

## Requirements
* [ExplorViz Backend](https://github.com/ExplorViz/explorviz-backend/tree/1.5.0) Version 1.5.0 
* [ExplorViz Frontend](https://github.com/ExplorViz/explorviz-frontend/tree/1.5.0) Version 1.5.0
* [ExplorViz Frontend Extension Heatmap](https://github.com/ExplorViz/explorviz-frontend-extension-heatmap)


## Setup Development / Usage

1. Follow the [README](https://github.com/ExplorViz/explorviz-backend/tree/1.5.0/README.md) of the [ExplorViz Backend](https://github.com/ExplorViz/explorviz-backend/tree/1.5.0).

2. Clone this repository

3. Import project into eclipse: via `Import -> Gradle -> Existing Gradle project -> path/to/explorviz-backend-extension-heatmap`

4. Start the required MongoDB instance or use the docker-compose file [from below](#Attention)

5. Start explorviz-backend-extension-heatmap from eclipse

6. Setup and start [ExplorViz Frontend](https://github.com/ExplorViz/explorviz-frontend/tree/1.5.0) with the installed [ExplorViz Frontend Extension Heatmap](https://github.com/ExplorViz/explorviz-frontend-extension-heatmap) as explained in the frontend extension.

## Attention

For the communication between frontend and backend extension it is required  to add the backend routes of the broadcast and reload resources to the Traefik configuration. In [./docker-compose/traefik](https://github.com/ExplorViz/explorviz-backend-extension-heatmap/tree/master/docker-compose/traefik) we provide a configuration file that can be used.

Furthermore, we provide a [docker-compose file](https://github.com/ExplorViz/explorviz-backend-extension-heatmap/blob/master/docker-compose/docker-compose.yml) to start the auxiliary services of the backend software stack alltogether.

If you want to use the backend separately you need to provide a MongoDB instance on port 27020 or configure the port in the [explorviz-properties](https://github.com/ExplorViz/explorviz-backend-extension-heatmap/blob/master/src/main/resources/explorviz.properties) of the extension..