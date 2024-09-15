FROM public.ecr.aws/liquibase/liquibase:4.28
COPY --chown=liquibase:liquibase liquibase.sh /liquibase/
RUN chmod +x /liquibase/liquibase.sh
COPY --chown=liquibase:liquibase changelog /liquibase/changelog
ENTRYPOINT ["/liquibase/liquibase.sh"]
