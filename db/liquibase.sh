#!/bin/bash
set -e
echo "***** Execute main operation *****"

liquibase --headless=true --url="${DB_URL}" --username=$DB_USER --password=$DB_PASSWORD --default-schema-name=$DB_SCHEMA "$@" --changelog-file=changelog/root-changelog.yml
echo "***** Operation completed *****"

echo "${DB_URL}"

if [[ x"${DB_TAG}" == "x" ]]; then
  echo "***** Setting tag is not required *****"
else
  echo "***** Set tag ${DB_TAG} *****"
  liquibase --headless=true --url="${DB_URL}" --username=$DB_USER --password=$DB_PASSWORD --default-schema-name=$DB_SCHEMA tag --tag=$DB_TAG
fi
echo "${DB_URL}"
echo "***** Check db status *****"
liquibase --headless=true --url="${DB_URL}" --changeLogFile=changelog/root-changelog.yml --username=$DB_USER --password=$DB_PASSWORD --default-schema-name=$DB_SCHEMA status