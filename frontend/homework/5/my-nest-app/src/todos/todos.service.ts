import { Injectable } from '@nestjs/common';
import { Todo } from './entities/todo.entity';
import { CreateTodoDto } from './dto/create-todo.dto';
import { TodoNotFoundException } from 'src/common/filters/all-exceptions.filter';

@Injectable()
export class TodosService {
    private todos: Todo[]=[];
    private nextId = 1;

    findAll():Todo[]{
        return this.todos;
    }

    findById(id:number): Todo{
        const todo=this.todos.find((t)=> t.id===id);
        if(!todo) throw new TodoNotFoundException(id);

        return todo;
    }

    create(dto:CreateTodoDto): Todo{
        const now =new Date();

        const todo: Todo={
            id:this.nextId++,
            title:dto.title,
            description:dto.description,
            completed:false,
            createdAt:now,
            updatedAt:now,
        };

        this.todos.push(todo);
        return todo;
    }

    deleteById(id:number): void{
        const exists = this.todos.find((t) => t.id===id);

    if (!exists) {
      throw new TodoNotFoundException(id);
    }
    this.todos = this.todos.filter((t) => t.id!==id);
    }
}
