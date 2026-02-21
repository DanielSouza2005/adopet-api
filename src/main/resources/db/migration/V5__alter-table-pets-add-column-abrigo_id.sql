ALTER TABLE PETS
    ADD COLUMN ADOCAO_ID BIGINT NULL;

ALTER TABLE PETS
    ADD constraint fk_pets_adocao_id
        foreign key (adocao_id)
            references abrigos (id);