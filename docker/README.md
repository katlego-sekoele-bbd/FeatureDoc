# Docker Setup

This directory contains the necessary files to set up and run the required services using Docker.

## Running the Services

To start the services, use the following command:

```sh
docker compose --env-file path/to/env up
```
Replace `path/to/env` with the actual path to your environment file. For example:
```sh
docker compose --env-file ../.env.local up
```


## Environment Variables

For the database to run correctly, ensure the following environment variables are set in your `.env` file:

```ini
# Database variables
POSTGRES_HOST=
POSTGRES_PORT=
POSTGRES_USER=
POSTGRES_PASSWORD=
POSTGRES_DB=
POSTGRES_SCHEMA=
```

Fill in the appropriate values before running the services.

## Notes
- Ensure Docker and Docker Compose are installed on your system.
- The `.env` file should be located in a secure place and not be committed to version control.
- If you encounter issues, check the Docker logs using:
  
  ```sh
  docker compose logs
  ```

