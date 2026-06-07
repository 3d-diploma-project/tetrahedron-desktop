package org.cmps.tetrahedron.view.home;

import org.cmps.tetrahedron.router.Page;
import org.cmps.tetrahedron.router.Router;

public class HomePage {

    public void openModelPage() {
        Router.getInstance().openPage(Page.MODEL);
    }

    public void openMeshPage() {
        Router.getInstance().openPage(Page.MESH);
    }
}
