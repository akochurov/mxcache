package com.maxifier.mxcache.impl.caches.def;

import gnu.trove.*;

import com.maxifier.mxcache.storage.*;

/**
 * Project: Maxifier
 * Created by: Yakoushin Andrey
 * Date: 15.02.2010
 * Time: 13:54:51
 * <p/>
 * Copyright (c) 1999-2009 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author ELectronic ENgine
 */

public class CharacterCharacterTroveStorage extends TShortShortHashMap implements CharacterCharacterStorage {
    public CharacterCharacterTroveStorage() {
    }

    public CharacterCharacterTroveStorage(TShortHashingStrategy strategy) {
        super(strategy);
    }

    @Override
    public boolean isCalculated(char o) {
        return super.contains((short)o);
    }

    @Override
    public char load(char o) {
        return (char)super.get((short)o);
    }

    @Override
    public void save(char o, char t) {
        put((short)o, (short)t);
    }
}