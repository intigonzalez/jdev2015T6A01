jdev2015T6A01
=============


Le but de cette partie est de décentraliser la partie storage de la box vers un
autre conteneur affin de libérer de la place.


Les points d'entrée REST
-------------

### Inscription (côté Box)

La partie storage va devoir « s’inscrire » sur la box afin que celle-ci soit au
courant de son existence. On utilisera le endpoints « api/thirdpartystorage »
affin de poster à la box l’adresse de redirection de la partie storage (comme
l’exemple si dessous) afin que celle-ci soit informée qu'un storage alternatif
est présent.

\- Cette adresse de redirection sera donc utilisée par la box afin de dire au
worker ou envoyer les vidéos traitées.

\- Elle sera aussi utilisée par la box en cas de requête de la part du front-end
afin d’obtenir les fichiers traités (le fichier original restera sur la box).

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
{
  "thirdPartyStorage": {
    "id": "42",
    "name": "HDD1",
    "url": "http://addr:8082/api/storage"
  }
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

### API de Stockage 

La partie stockage devra donc pouvoir répondre aux requêtes faites par la box.  
La première sera de répondre au POST fait par le worker via l’adresse que vous
avez passée à l’inscription.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
{POST} http://addr:8082/api/storage/{contentId}/{resolution}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La seconde sera de récupérer les données sauvegardées par le worker pour le
front-end.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
{GET} http://addr:8082/api/storage/{contentId}/{resolution}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

### Le Code

Le code devra simplement sauvegarder les données et bien sûr pouvoir les
récupérer.
