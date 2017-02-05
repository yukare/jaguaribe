/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.common;

import java.awt.event.ActionEvent;

/**
 * Interface for all classes that use RecentItensMenu.
 *
 * @author Fernando Correa da Conceição
 */
public interface RecentMenuInterface {

    /**
     * When one of the itens in menu is clicked
     *
     * @param item The item clicked.
     * @param event The event.
     */
    void recentMenuItemClicked(RecentItem item, ActionEvent event);
}
