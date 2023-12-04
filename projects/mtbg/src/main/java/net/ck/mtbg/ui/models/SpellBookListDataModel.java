package net.ck.mtbg.ui.models;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.Player;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.components.SpellBookDataModelDataListener;

import javax.swing.*;

@Log4j2
public class SpellBookListDataModel
        extends DefaultListModel<AbstractSpell>
{
    public SpellBookListDataModel()
    {
        filterSpellsByLevel();
        addListDataListener(new SpellBookDataModelDataListener());
    }

    private static ListModel<AbstractSpell> _createCurrentSpellsModel()
    {
        final DefaultListModel<AbstractSpell> model = new DefaultListModel<>();
        _updateSpellsBySelectedLevel(model);
        return model;
    }

    private static void _updateSpellsBySelectedLevel(DefaultListModel<AbstractSpell> model)
    {
        model.removeAllElements();

        final Player player = Game.getCurrent().getCurrentPlayer();
        final int selectedSpellLevel = player.getSelectedSpellLevel();

        model.addAll(
                player.getSpells().stream()
                        .filter(spell -> spell.getLevel() == selectedSpellLevel)
                        .toList()
        );
    }

    // fixme
    //  move this logic to a "controller"
    //  consider renaming -> updateSpellsBySelectedLevel
    public void filterSpellsByLevel()
    {
        logger.debug(() -> "update spells");
        _updateSpellsBySelectedLevel(this);
    }
}
