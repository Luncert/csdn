from unit_test import TestManager, test_manager

test_manager = TestManager()

if __name__ == "__main__":
    import argparse
    import os
    from os import path as Path

    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers(help='commands')

    # unit test commands
        
    def load_scripts(base):
        with open(Path.join(base, '__init__.py'), 'wb'):
            pass
        for name in os.listdir(base):
            path = Path.join(base, name)
            if Path.isdir(path) and not name == '__pycache__':
                load_scripts(path)
            elif not name.startswith('__init__') and name.endswith('.py'):
                import_path = path.replace('\\', '.').replace('/', '.').replace('.py', '')
                test_manager.set_loadpath(import_path)
                __import__(import_path, fromlist=['*'])

    def unit_test(args):
        if not Path.exists('src'):
            raise Exception('could not found src directory in cwd')
        if not Path.isdir('src'):
            raise Exception('src is not a directory')
        load_scripts('src')
        if args.unit:
            test_manager.test(args.unit)


    run_parser = subparsers.add_parser('unit_test', help='run unit test')
    run_parser.add_argument('--unit',      '-u',   help='specify target test unit', required=True)
    run_parser.set_defaults(func=unit_test)

    # parse

    args = parser.parse_args()
    args.func(args)
    