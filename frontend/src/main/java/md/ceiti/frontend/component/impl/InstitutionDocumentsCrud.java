package md.ceiti.frontend.component.impl;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;
import md.ceiti.frontend.component.CrudComponent;
import md.ceiti.frontend.component.DMFormFactory;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.dto.institution.CategoryDto;
import md.ceiti.frontend.dto.institution.DocumentDto;
import md.ceiti.frontend.exception.CrudException;
import md.ceiti.frontend.service.impl.InstitutionCategoryService;
import md.ceiti.frontend.service.impl.InstitutionDocumentService;
import md.ceiti.frontend.util.IOUtils;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.List;

public class InstitutionDocumentsCrud extends VerticalLayout implements CrudComponent<DocumentDto> {

    private final InstitutionDocumentService institutionDocumentService;
    private final InstitutionCategoryService institutionCategoryService;
    private final boolean hasWriteAccess;

    public InstitutionDocumentsCrud(InstitutionDocumentService institutionDocumentService,
                                    InstitutionCategoryService institutionCategoryService,
                                    boolean hasWriteAccess) {
        this.institutionDocumentService = institutionDocumentService;
        this.institutionCategoryService = institutionCategoryService;
        this.hasWriteAccess = hasWriteAccess;
        buildComponent();
    }

    @Override
    public void buildComponent() {
        add(getGridCrud());
    }

    @Override
    public GridCrud<DocumentDto> getGridCrud() {
        GridCrud<DocumentDto> crud = DMFormFactory.getDefaultCrud(
                DocumentDto.class,
                institutionDocumentService,
                List.of("id"),
                List.of("createdBy", "createdAt", "extension")
        );

        List<CategoryDto> categories = institutionCategoryService.findAll()
                .stream()
                .filter(CategoryDto::isEnabled)
                .toList();

        DMFormFactory.setFieldProvider(crud,
                categories,
                "category",
                "name");
        DMFormFactory.orderColumns(crud, DocumentDto.class, List.of("createdBy", "createdAt", "extension"));
        crud.getAddButton().setVisible(hasWriteAccess);
        crud.getUpdateButton().setVisible(hasWriteAccess);
        crud.getDeleteButton().setVisible(hasWriteAccess);

        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);
        upload.setMaxFiles(1);
        upload.setDropAllowed(true);
        upload.setAcceptedFileTypes(
                "application/pdf",
                ".pdf",
                ".doc",
                ".docx",
                ".txt",
                ".xls",
                ".xlsx",
                ".ppt",
                ".pptx"
        );

        crud.getAddButton().addClickListener(event -> {
            FormLayout formLayout = DMFormFactory.getForm(crud.getCrudFormFactory());
            formLayout.add(upload);
        });

        crud.setAddOperation(documentDto -> {
            if (memoryBuffer.getFileData() == null) {
                throw new CrudException("A document must be uploaded");
            }

            try {
                return institutionDocumentService.insert(documentDto,
                        IOUtils.toFileSystemResource(
                                memoryBuffer.getFileName(),
                                memoryBuffer.getInputStream().readAllBytes()));
            } catch (Exception e) {
                throw new CrudException();
            }
        });

        GridContextMenu<DocumentDto> contextMenu = crud.getGrid().addContextMenu();
        contextMenu.addItem(I18n.Component.DOWNLOAD, event -> event.getItem().ifPresent(item -> {
            String fileName = item.getName() + "." + item.getExtension();
            byte[] bytes = institutionDocumentService.download(item);
            StreamResource streamResource = IOUtils.toStreamResource(
                    fileName,
                    bytes
            );

            Anchor anchor = new Anchor(streamResource, "download");
            anchor.getElement().setAttribute("download", fileName);
            anchor.getStyle().set("visibility", "hidden");
            add(anchor);
            anchor.getElement().callJsFunction("click").then(ignored -> remove(anchor));
        }));

        return crud;
    }
}
