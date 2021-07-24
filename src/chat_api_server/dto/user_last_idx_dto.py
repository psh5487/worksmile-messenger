class UserLastIdxDto:
    def __init__(self):
        self.uid = None
        self.uname = None
        self.profile = None
        self.role = None
        self.cid = None
        self.cname = None
        self.subroot_cid = None
        self.subroot_cname = None
        self.root_cid = None
        self.root_cname = None
        self.pid = None
        self.pname = None
        self.email = None
        self.phone = None
        self.register_at = None
        self.login_at = None
        self.all_push_notice = None
        self.last_read_idx = None
 
    def __str__(self):
        return self.uid