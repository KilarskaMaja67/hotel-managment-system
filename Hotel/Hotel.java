package Hotel;

import Room.Room;

import java.util.Arrays;

abstract class Hotel {

    String town;
    String name;
    int stars;
    String address;
    Room[] rooms;



    public Hotel(String town, String name, int stars, String address,  Room[] rooms) {
        this.town=town;
        this.name=name;
        this.stars=stars;
        this.address=address;
        this.rooms=rooms;

    }

    public String getTown() {
        return town;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }

    public String getAddress() {
        return address;
    }



	public Room[] getRooms() {
		return rooms;

	}




    public String toString() {
        return "miejscowosc:  "+ town + " nazwa: " + name + " gwiazdki: " + stars + " adres: " + address + '\n'
                + Arrays.toString(rooms) ;
    }


    public void setTown(String town) {
        this.town= town;
    }

    public void setName(String name) {
        this.name= name;
    }

    public void setStars(int stars) {
        this.stars= stars;
    }

    public void setAddress(String address) {
        this.address= address;
    }


    public void setRooms(Room[] rooms) {
        this.rooms= rooms;
    }






}










