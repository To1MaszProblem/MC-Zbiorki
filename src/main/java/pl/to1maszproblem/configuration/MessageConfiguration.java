package pl.to1maszproblem.configuration;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import pl.to1maszproblem.notice.Notice;
import pl.to1maszproblem.notice.NoticeType;

@Getter
public class MessageConfiguration extends OkaeriConfig {

    private String holdItemToCreate = "&cMusisz trzymać item w ręce aby stworzyć zbiórkę!";
    private String collectionAlreadyExist = "&cZbiórka o takiej nazwie już istnieje!";
    private String createdCollection = "&7Pomyslnie stworzono zbiórkę o nazwie &f[collection-name]!";
    private String collectionDoesntExist = "&cZbiórka o takiej nazwie nie istnieje!";
    private String deletedCollection = "&7Pomyslnie usunięto zbiórkę o nazwie &f[collection-name]!";
    private Notice collectionEnd = new Notice(NoticeType.MESSAGE,"&7Pomyslnie ukończono zbiurkę &f[collection-name]!");
    private Notice payCollection = new Notice(NoticeType.MESSAGE,"&7Pomyslnie wpłacono [amount] przedmiot na zbiurkę &f[collection-name]!");
    private Notice doesntHaveCollectionItem = new Notice(NoticeType.MESSAGE,"&cNie posiadasz tego przedmiotu lub wystarczającej jego ilości!");
    private Notice doesntSetedCollectionItem = new Notice(NoticeType.MESSAGE,"&cPrzedmiot do zbierania nie został ustawiony!");
}
