
class TreeNode(object):
    def __init__(self, name, value):
        self.name = name
        self.value = value
        self.children = []

    def add_child(self, child, match_path=None):
        if match_path:
            self.match_or_create(match_path).add_child(child)
        else: self.children.append(child)

    def match(self, name):
        return self.name == name

    def match_children(self, name):
        for child in self.children:
            if child.match(name):
                return child
    
    def match_all(self, path):
        base = self
        names = path if isinstance(path, list) else path.split('.')
        if not base.match(names[0]):
            return None

        names.pop(0)
        for name in names:
            base = base.match_children(name)
            if not base: return None
        return base
    
    def match_all_from_children(self, path):
        names = path if isinstance(path, list) else path.split('.')
        base = self.match_children(names[0])
        if not base: return None

        names.pop(0)
        for name in names:
            base = base.match_children(name)
            if not base: return None
        return base

    def match_or_create(self, path):
        tmp = base = self
        names = path if isinstance(path, list) else path.split('.')
        if not base.match(names[0]):
            return None

        i, limit = 1, len(names)
        while i < limit:
            name = names[i]
            base = base.match_children(name)
            if not base:
                base = tmp
                while i < limit:
                    name = names[i]
                    tmp = TreeNode(name, None)
                    base.add_child(tmp)
                    base = tmp
                    i += 1
                break
            else: i += 1
        return base

    def __str__(self):
        return 'TreeNode(name=%s, value=%s)' % (self.name, self.value)
    
    def print_tree(self, indent=''):
        print(indent + self.__str__())
        indent += '  '
        for child in self.children:
            child.print_tree(indent)


class TestManager(object):
    def __init__(self):
        self.tests = TreeNode('src', None)
        self.loadpath = None

    def add_test(self, func):
        tmp = self.tests.match_or_create(self.loadpath)
        if not tmp.value:
            tmp.value = {}
        tmp.value[func.__name__] = func
    
    def test(self, path):
        def raise_exception():
            raise Exception('cound not find test unit with specify path: ' + path)
        i = path.find('#')
        unit_name = None
        if i >= 0:
            names = path[:i].split('.')
            unit_name = path[i + 1:]
        else: names = path.split('.')
        names.insert(0, 'src')
        base = self.tests.match_all(names)
        
        if not base or not base.value:
            raise_exception()
        if unit_name:
            if not unit_name in base.value:
                raise_exception()
            base.value[unit_name]()
        else:
            for test in base.value.values():
                test()
    
    def set_loadpath(self, loadpath):
        self.loadpath = loadpath

test_manager = None

# Annotation
def test(func):
    if test_manager:
        test_manager.add_test(func)
