package com.cyworld.social.utils.request.guild;

import lombok.Data;

@Data
public class req_set_pos {
    String player_id;
    String player_namespace;
    String guid_id;
    String target_id;
    String target_namespace;
    int pos;
    int max_count_lvl2;
    int max_count_lvl3;
}
