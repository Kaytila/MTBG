package net.ck.mtbg.ui.models.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.entities.entities.Player;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.ui.listeners.game.SpellBookDataModelDataListener;

import javax.swing.*;

@Getter
@Setter
@Log4j2
public class SpellBookListDataModel extends DefaultListModel<AbstractSpell>
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
