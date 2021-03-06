package com.geotask.myapplication.DataClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.geotask.myapplication.Controllers.Helpers.HashSetConverter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 *	data stucture for a task
 * stores general needed information and the Ids of people related to the task
 *
 * Resources:
 *
 * 		https://stackoverflow.com/questions/12960265/retrieve-all-values-from-hashmap-keys-in-an-arraylist-java
 * 			for basic arraylist operations
 * 			Author Rohit Jain, Oct 18, 2012, no licence stated
 */
@Entity(tableName = "tasks")
public class Task extends GTData implements Comparable{
	@ColumnInfo
	private String name;
	@ColumnInfo
	private String description;
	@ColumnInfo //ToDo change type to Enum
	private String status;
	@ColumnInfo
	private String photoList;
	@TypeConverters(HashSetConverter.class)
	private HashSet<String> bidList = new HashSet<>();
	@ColumnInfo
	private Double acceptedBid;
	@ColumnInfo
	private String acceptedBidID;
	@ColumnInfo(name = "requesterId")
	private String requesterID;
	@ColumnInfo
	private String acceptedProviderID;
	@ColumnInfo
	private int hitCounter;
	@ColumnInfo
	private String location;
	@ColumnInfo
	private Double lowestBid;
	@ColumnInfo
	private Integer numBids;
	@ColumnInfo
	private transient boolean editedFlag;

	public String getLocation() {
		return location;
	}

	public Double getLocationX() {
		System.out.println(location);
		if (location == null || location.equals("null")) {
			return -1.0;
		}
		else {
			return Double.parseDouble((location.split("[,]")[0]));
		 }
	}

	public Double getLocationY() {
		System.out.println(location);
		if(location == null || location.equals("null")) {
			return -1.0;
		}
		else {
			return Double.parseDouble((location.split("[,]")[1]));
		}
	}

	public void setLocation(String location) {
		this.location = location;
	}
	//ToDo pictures


	//@Ignore
	public Task(String requesterID, String name, String description, String location) { //need string for pictures
		super.setType(Task.class.toString());
		this.name = name;
		this.description = description;
		this.hitCounter = 0;
		this.status = "Requested";
		super.setDate(new Date().getTime());
		this.acceptedBid = -1.0; //ToDo
		this.requesterID = requesterID;
		this.location = location;
		this.lowestBid = -1.0;
		this.numBids = 0;
		editedFlag = false;
	}

	/**
	 *constructor for task
	 * gets reuqiester ID, anme and the description. locatiion is later to be inplemented by overload
	 * @param requesterID
	 * @param name
	 * @param description
	 */
	@Ignore
	public Task(String requesterID, String name, String description) { //need string for pictures
		super.setType(Task.class.toString());
		this.name = name;
		this.description = description;
		this.hitCounter = 0;
		this.status = "Requested";
		super.setDate(new Date().getTime());
		this.acceptedBid = -1.0; //ToDo
		this.requesterID = requesterID;
		this.lowestBid = -1.0;
		this.numBids = 0;
		editedFlag = false;
	}

	/**
	 *gets nae
	 * @return this.name
	 */
    public String getName() {
		return this.name;
	}
	/**
	 *sets Name
	 * @param Name
	 */
	public void setName(String Name) {
		this.name = Name;
	}
	/**
	 *gets description
	 * @return this.description
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 *sets description
	 * @param Description
	 */
	public void setDescription(String Description) {
		this.description = Description;
	}

	/**
	 *gets status
	 * @return this.status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 *set status
	 * @param Status
	 */
	public void setStatus(String Status) {
		this.status = Status;
	}

	/**
	 *gets list of pictures
	 * @return this.photoList
	 */
	public String getPictures() {
		return this.photoList;
	}
	/**
	 *sets a picture
	 * @param Picture
	 */
	public void setPicture(String Picture) {
		this.photoList= Picture;
	}

	/**
	 *sets the ammount of accepted Bid
	 * @param Bid
	 */
	public void setAcceptedBid(Double Bid) {
		this.acceptedBid = Bid;
	}
	/**
	 *get ammount of the accepted bid
	 * @return this.acceptedBid
	 */
	public Double getAcceptedBid() {
		return this.acceptedBid;
	}

	/**
	 * sets  the requester ID
	 * @param Provider
	 */
	public void setRequesterID(String Provider) {
		this.requesterID = Provider;
	}

	/**
	 * gets the requesterId
	 * @return this.requesterID
	 */
	public String getRequesterID(){
		return this.requesterID;
	}

	/**
	 * sets the provider ID from the accepted bid
	 * @param Requester
	 */
	public void setAcceptedProviderID(String Requester) {
		this.acceptedProviderID = Requester;
	}

	/**
	 * gets the provider Id of the requested bid
	 * @return this.acceptedProviderID
	 */
	public String getAcceptedProviderID() {
		return this.acceptedProviderID;
	}

	/**
	 * increment it counter
	 */
	public void addHit(){
		this.hitCounter ++;
	}
	/**
	 *get hit counter ammount
	 */
	public Integer getHitCounter(){
		return this.hitCounter;
	}
	/**
	 *gets the number of biders
	 * @return bidList.size()
	 */
	public Integer getNumBidders(){
		return this.bidList.size();
	}

	/**
	 *add a bid id to bids associated with task
	 * @param bid
	 */
	public void addBid(Bid bid){
		bidList.add(bid.getObjectID());
	}

	public String getPhotoList() {
		return photoList;
	}

	/**
	 *gets list of bids
	 * @return bidList
	 */
	public HashSet<String> getBidList() {
		return bidList;
	}
    /**
     * deletes a bid given to the bidlist
     * @param bid
     */
    public void deleteBid(Bid bid){
        bidList.remove(bid.getObjectID());
    }


	/**
	 *sets bidlist from given value
	 * @param bidList
	 */
	public void setBidList(HashSet<String> bidList) {
		this.bidList = bidList;
	}



	/**
	 *sets the hitcounter to specified ammount
	 * @param hitCounter
	 */
	public void setHitCounter(int hitCounter) {
		this.hitCounter = hitCounter;
	}

	/**
	 *gets the id of the accpted bid
	 */
	public String getAcceptedBidID() {
		return acceptedBidID;
	}

	/**
	 *sets Id of the accpted Bid
	 * @param acceptedBidID
	 */
	public void setAcceptedBidID(String acceptedBidID) {
		this.acceptedBidID = acceptedBidID;
	}

	/**
	 *sets the status to Requested
	 */
	public void setStatusRequested(){
		this.status = "Requested";
	}

	/**
	 *sets the status to Accepted
	 */
	public void setStatusAccepted(){
		this.status = "Accepted";
	}

	/**
	 *sets the status to Completed
	 */
	public void setStatusCompleted(){
		this.status = "Completed";
	}

	/**
	 *sets the status to Bidded
	 */
	public void setStatusBidded(){
		this.status = "Bidded";
	}

	/**
	 *retunrs all items in string format
	 * @return  this.name + " " + this.description + " " + bidList.toString()
	 */
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public Double getLowestBid() {
		return lowestBid;
	}

	public void setLowestBid(Double lowestBid) {
		this.lowestBid = lowestBid;
	}

	public Integer getNumBids() {
		return numBids;
	}

	public void setNumBids(Integer numBids) {
		this.numBids = numBids;
	}

	@Override
	public int compareTo(@NonNull Object o) {
		Task other = (Task) o;
		int ret = (int) ((int) this.getDate() - other.getDate());
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void setPhotoList(String photoList) {
		this.photoList = photoList;
	}

	public boolean isEditedFlag() {
		return editedFlag;
	}

	public void setEditedFlag(boolean editedFlag) {
		this.editedFlag = editedFlag;
	}

}
