package org.orury.domain.global.listener;


import org.orury.common.util.S3Folder;

public class CrewImageConverter extends EntityImageConverter {
    @Override
    protected S3Folder domainName() {
        return S3Folder.CREW;
    }
}
