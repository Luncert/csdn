package org.luncert.csdn2.model;

public enum Category
{
    Career, Web, Arch, Lang, Db, Game, Mobile, Ops, Ai,
    Sec, Cloud, Engineering, Iot, Fund, Avi, Other;

    public String getName()
    {
        return toString().toLowerCase();
    }

}