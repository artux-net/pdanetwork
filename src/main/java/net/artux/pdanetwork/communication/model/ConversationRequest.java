package net.artux.pdanetwork.communication.model;

import java.util.ArrayList;
import java.util.List;

public class ConversationRequest {

  public int cid;
  public String title = "";
  public List<Integer> owners = new ArrayList<>();
  public List<Integer> deleteOwners = new ArrayList<>();
  public List<Integer> members = new ArrayList<>();
  public List<Integer> deleteMembers = new ArrayList<>();

}
