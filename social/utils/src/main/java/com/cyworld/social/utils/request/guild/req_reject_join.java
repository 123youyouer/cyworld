package com.cyworld.social.utils.request.guild;

import lombok.Data;

@Data
public class req_reject_join {
    String player_id;
    String player_namespace;
    String guid_id;
    String target_id;
    String target_namespace;
    boolean reject_all=false;
}
