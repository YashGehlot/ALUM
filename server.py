#server for seller
from bottle import route, run, template,response, get, post,request, static_file
# import base64
# import math
import os
import json
from json import dumps, load


folder_name = "seller_DB/"
#Save the sellers data
def saveProfileSeller(Username,Phone,shopname,Address):
		set_Profile_Seller(Username,Phone,shopname,Address)		
		print "User saved"

#logic to save profile in database	
def set_Profile_Seller(Username,Phone,shopname,Address):    
		Inventory = "nothing"
		Special_offers = "No special offers for today"
		filename = folder_name+ Username
		with open(filename, "w") as file:
			json.dump({'Username':Username, 'Phone':Phone, 'shopname':shopname, 'Address':Address,
			'Inventory':Inventory, 'offers':Special_offers}, file, indent=4)
		file.close()	

#Save the sellers data
def saveProfile(Username,Phone):
		set_Profile(Username,Phone)		
		print "User saved"
	# filename = folder_name+ Username
	# with open(filename) as data_file:    
 #    		data = json.load(data_file)
 #   	print data["Username"]
	#pass

#logic to save profile in database
def set_Profile(Username,Phone):    
		Inventory = ",nothing"
		filename = folder_name+ Username
		with open(filename, "w") as file:
			json.dump({'Username':Username, 'Inventory':Inventory, 'Phone':Phone }, file, indent=4)
		file.close()	
	#	pass

def cross_check_lists(Seller, Buyer):
		
		seller_file = folder_name + Seller
		buyer_file = folder_name + Buyer

		seller_info = open(seller_file, "r")
		buyer_info = open(buyer_file, "r")
		
		buyer_inven = json.load(buyer_info)
		seller_inven = json.load(seller_info)
		
		seller_inventory = str(seller_inven["Inventory"])
		buyer_inventory = (str(buyer_inven["Inventory"]))[1:]
		
		seller_address = seller_inven["Address"]
		seller_shopname = seller_inven["shopname"]
		special_offers = seller_inven["offers"]
		#print seller_inventory
		seller = seller_inventory.split(",")
		buyer = buyer_inventory.split(",")
		
		seller_info.close()
		buyer_info.close()
		
		print buyer
		#print buyer_inventory
		c = []
		for bx in seller:
    			if bx in buyer:
        				c.append(bx)
		print c
		if len(c)!=0: 
			d = c[0]
		else: d = "No items"	
		s = d +"$"+  seller_shopname +"$"+ seller_address + "$" + special_offers 
		#else: s = "not_matched"	
		return s


def update_buyer_inventory(User, Inventory):
		
		filename = folder_name+ User
		print("filename = " + filename)
		# print filename+ " ,"+ User + ","
		with open(filename, "r") as jsonFile:
	    		data = json.load(jsonFile)
				#tmp = data["Inventory"]
		data["Inventory"] = Inventory
		with open(filename, "w") as jsonFile:
	    		json.dump(data, jsonFile, indent = 4)
	    	jsonFile.close()

def update_seller_inventory(Username, Inventory):
		#Username = "yogesh"
		#Inventory = "toothbrush"
		filename = folder_name+ Username
		with open(filename, "r") as jsonFile:
	    		data = json.load(jsonFile)
				#tmp = data["Inventory"]
		data["Inventory"] = Inventory
		with open(filename, "w") as jsonFile:
	    		json.dump(data, jsonFile, indent = 4)
	    	jsonFile.close()

def update_seller_offer(username, offer):
		
		filename = folder_name+ username
		with open(filename, "r") as jsonFile:
	    		data = json.load(jsonFile)
				#tmp = data["Inventory"]
		data["offers"] = offer
		with open(filename, "w") as jsonFile:
	    		json.dump(data, jsonFile, indent = 4)
	    	jsonFile.close()	
	
###########################	  

@post('/set_Profile')
def login_page():	
	Username = request.forms.get('Username')
	#name = request.forms.get('Fullname')
	Phone = request.forms.get('Phone')
	shopname = request.forms.get('shopname')
	Address = request.forms.get('Address')
	print Username
	saveProfileSeller(Username,Phone,shopname,Address)
	return "Yes"

@post('/buyer_set_profile')
def login_page():	
	Username = request.forms.get('Username')
	Phone = request.forms.get('Phone')
	#image = request.forms.get('Image')
	print Username
	saveProfile(Username,Phone)
	return "Yes"

@post('/buyer_update_inventory')
def login_page():
	Username = request.forms.get('Username')
	Inventory = request.forms.get('Inventory')
	print Username
	update_buyer_inventory(Username, Inventory)
	#print type(Username)
	#print Username
	#print Inventory
	return "Yes"

@post('/seller_update_inventory')
def login_page():
	Username = request.forms.get('Username')
	Inventory = request.forms.get('Inventory')
	#print type(Username)
	print Username
	#print Inventory

	update_seller_inventory(Username, Inventory)
	#print type(Username)
	print Inventory

	return "Yes"

@post('/look_for_seller')
def login_page():
	Seller = request.forms.get("hotspot")
	Buyer = request.forms.get("Username")
	#print Seller
	#print type(Seller)
	#print Buyer
	#print type(Buyer)
	s = cross_check_lists(Seller, Buyer)
	print s 
	return s

@post('/test')
def login_page():
	wifiSSID = request.forms.get("Username")
	print wifiSSID
	return "YES"

@post('/seller_offer')
def login_page():
	usrnm = request.forms.get("Username")
	offer = request.forms.get("Offer")
	print usrnm
	print offer
	update_seller_offer(usrnm, offer)
	return "Yes"



run(host='10.42.0.1', port=8081, debug=True)  
