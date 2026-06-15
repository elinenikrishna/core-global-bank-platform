.PHONY: install build test dev infra down demo-seed

install:
	cd frontend && npm install

build:
	cd frontend && npm run build
	cd backend && mvn clean package

test:
	cd frontend && npm run lint
	cd backend && mvn test

dev:
	cd frontend && npm run dev

infra:
	docker compose up -d postgres kafka cassandra redis

down:
	docker compose down

demo-seed:
	curl -X POST "http://localhost:8091/api/v1/admin/seed-jobs"
