# Java Application Deployment on Minikube using Docker and Helm

## Objective

To design and validate a deployment workflow for a Java-based microservice on a Kubernetes cluster (Minikube) using Docker containerization and Helm charts, while implementing a version-controlled release mechanism.

## Technologies Used

* Java (Spring Boot)
* Maven
* Docker
* Kubernetes (Minikube)
* Helm
* Git (version control)

## Application Enhancements

The Java application was modified to expose operational metadata useful during deployments and troubleshooting.

### Implemented Endpoints

| Endpoint   | Purpose                                          |
| ---------- | ------------------------------------------------ |
| `/`        | Basic service validation                         |
| `/health`  | Health status check                              |
| `/version` | Returns running application version and pod name |

Example response:

```json
{
  "application": "java-demo",
  "version": "1.0.0",
  "pod": "java-release-java-app-7f4b7c9b6f-x82pl"
}
```

This allows operators to confirm which application version is currently running inside the cluster.

## Version Management Strategy

A `VERSION` file was introduced at the repository root to serve as the **single source of truth for release versions**.

Example:

```
VERSION
1.0.0
```

This version is used for:

* Docker image tagging
* Helm deployment configuration
* Application version exposure via `/version` endpoint

## Containerization

The Java application is packaged as a Docker container using a lightweight runtime image.

Key steps:

1. Build application

```
mvn clean package
```

2. Build container image

```
docker build -t java-app:<VERSION> .
```

The container exposes port `8080` for HTTP requests.

## Kubernetes Deployment using Helm

A Helm chart was created to manage Kubernetes resources.

### Helm Chart Components

| File              | Purpose                     |
| ----------------- | --------------------------- |
| `Chart.yaml`      | Chart metadata              |
| `values.yaml`     | Deployment configuration    |
| `deployment.yaml` | Pod deployment definition   |
| `service.yaml`    | Kubernetes service exposure |

The Docker image tag is dynamically set from the `VERSION` file.

Deployment command:

```
helm upgrade --install java-release ./helm/java-app --set image.tag=<VERSION>
```

## Deployment Workflow

### Step 1: Build Application

```
cd app
mvn clean package
```

### Step 2: Build Docker Image

```
eval $(minikube docker-env)

VERSION=$(cat VERSION)

docker build -t java-app:$VERSION -f docker/Dockerfile .
```

### Step 3: Deploy via Helm

```
helm upgrade --install java-release ./helm/java-app --set image.tag=$VERSION
```

### Step 4: Access Service

```
minikube service java-release-java-app
```

## Release Validation

After deployment, the running version can be verified:

```
curl http://SERVICE_URL/version
```

This confirms:

* running application version
* Kubernetes pod instance

## Release Upgrade Process

1. Update version in `VERSION` file.

```
1.0.1
```

2. Rebuild container image.

3. Upgrade Helm release.

helm upgrade java-release ./helm/java-app --set image.tag=1.0.1

Kubernetes performs a rolling update to replace existing pods.

## Rollback Capability

Helm provides built-in rollback functionality.

helm history java-release
helm rollback java-release <revision>

This restores a previously working release.

## Outcome

The R&D successfully demonstrated:

* containerization of a Java application
* Kubernetes deployment using Helm
* version-controlled release process
* runtime verification of deployed versions
* rolling updates and rollback capability

## Conclusion

The implemented workflow represents a simplified DevOps release pipeline where application versioning, container builds, and Kubernetes deployments are coordinated through a central `VERSION` file. This approach improves release traceability and simplifies upgrade and rollback operations.
