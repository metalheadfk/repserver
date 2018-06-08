#!/bin/sh
dbaccess -e - Env.sql
dbaccess -e - JDBC.sql
dbaccess -e - LO.sql
dbaccess -e - Circle.sql
