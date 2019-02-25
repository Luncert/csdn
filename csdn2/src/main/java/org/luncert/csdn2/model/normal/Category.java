package org.luncert.csdn2.model.normal;

public enum Category
{
    Career, Web, Arch, Lang, Db, Game, Mobile, Ops, Ai,
    Sec, Cloud, Engineering, Iot, Fund, Avi, Other;

    public static final Category[] categories = {
        Career, Web, Arch, Lang, Db, Game, Mobile, Ops, Ai,
        Sec, Cloud, Engineering, Iot, Fund, Avi, Other
    };

    public String getName()
    {
        return toString().toLowerCase();
    }

}