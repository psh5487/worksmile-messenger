class RoomUserDto:
    def __init__(self):
        self.uid = None
        self.ruuid = None
        self.rname = None
        self.favorite_type = None
        self.push_notice = None
        self.last_read_idx = None
 
    def __str__(self):
        return self.uid
