package domainModel.Search;

import java.io.Serializable;

/**
 * Interfaccia che rappresenta un'operazione di ricerca generica.
 * Implementata dai vari Decorator per aggiungere criteri alla ricerca.
 */
public interface Search extends Serializable {

    /**
     * Restituisce la query SQL per eseguire la ricerca.
     *
     * @return una stringa rappresentante la query SQL con i criteri applicati.
     */
    String getSearchQuery();
}