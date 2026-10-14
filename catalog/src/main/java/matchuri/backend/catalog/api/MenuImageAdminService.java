package matchuri.backend.catalog.api;

import matchuri.backend.catalog.result.AdminMenuItemDetailResult;
import matchuri.backend.catalog.result.MenuImageResult;
import org.springframework.web.multipart.MultipartFile;

public interface MenuImageAdminService {

    AdminMenuItemDetailResult getAdminMenuItemDetail(Long menuItemId);

    MenuImageResult uploadPrimaryImage(Long menuItemId, MultipartFile file);

    void deletePrimaryImage(Long menuItemId);
}
