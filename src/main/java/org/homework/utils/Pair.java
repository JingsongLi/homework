package org.homework.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by jslee on 2015/6/22.
 */
@Data
@AllArgsConstructor
public class Pair<K,V> {
    K k;
    V v;
}
