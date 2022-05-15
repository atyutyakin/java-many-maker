package pro.alxerxc.menuMaker.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.alxerxc.menuMaker.entity.Category;
import pro.alxerxc.menuMaker.entity.Persistable;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.service.GenericCrudService;
import pro.alxerxc.menuMaker.support.Pagination;

import javax.validation.Valid;

@Getter
@Setter
@ConfigurationProperties(prefix = "pro.alxerxc.menu-maker.controller")
public class GenericCrudController<E extends Persistable<ID>, ID> {
    private String redirectToListCommand;
    private String listViewName;
    private String editViewName;
    private String detailsViewName;

    private int defaultPageSize = 15;
    private int maxPaginationLinks = 10;

    private final GenericCrudService<E, ID> service;

    public GenericCrudController(GenericCrudService<E, ID> service, String viewsGroup, String entityNameInUrl) {
        this.service = service;
        this.redirectToListCommand = "redirect:/" + entityNameInUrl + "/all";
        this.listViewName = "/" + viewsGroup + "/index";
        this.editViewName = "/" + viewsGroup + "/edit";
        this.detailsViewName = "/" + viewsGroup + "/view";
    }

    public String showAllEntities(
                MultiValueMap<String, String> allRequestParams,
                Model model) {
        Pagination.Params params = Pagination.Params.of(allRequestParams);
        if (!params.contains("sort")) {
            params = params.put("sort", "id,asc");
        }

        Page<E> aPage = service.findPage(params.searchPattern(), params.pageIndex(),
                actualPageSize(params.pageSize()), Pagination.sort(params.sort()));

        model.addAttribute("entities", aPage.getContent());
        model.addAttribute("pagination", Pagination.of(aPage, params, maxPaginationLinks));
        return listViewName;
    }

    public String showNewEntityForm(E entity, BindingResult result, Model model) {
        setIsNew(model);
        setEntityAndBindingResult(model, entity, result);
        return editViewName;
    }

    public String showDetailsForm(ID id, Model model) {
        model.addAttribute("entity", service.findById(id));
        return detailsViewName;
    }

    public String showUpdateForm(ID id, Model model) {
        Persistable entity = service.findById(id);
        model.addAttribute("entity", entity);
        return editViewName;
    }

    public String addNewEntity(E product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showNewEntityForm(product, result, model);
        }
        service.add(product);
        return redirectToListCommand;
    }

    public String updateExistingEntity(ID id, E entity, BindingResult result, Model model) {
        if (!entity.hasId()) {
            throw new IllegalArgumentException("Id should be specified in entity's data");
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException("Specified Id in entity's data should match id in URL");
        }

        if (result.hasErrors()) {
            setEntityAndBindingResult(model, entity, result);
            return editViewName;
        }
        service.update(entity);
        return redirectToListCommand;
    }

    public String deleteEntity(ID id, Model model) {
        service.deleteById(id);
        return redirectToListCommand;
    }

    private void setIsNew(Model model) {
        model.addAttribute("isNew", Boolean.TRUE);
    }

    private void setEntityAndBindingResult(Model model, E entity, BindingResult result) {
        model.addAttribute("entity", entity);
        model.addAttribute("org.springframework.validation.BindingResult.entity", result);
    }

    private int actualPageSize(int size) {
        return (size != 0) ? size : defaultPageSize;
    }
}
